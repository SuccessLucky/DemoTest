package com.kzksmarthome.common.lib.cmd;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.kzksmarthome.common.module.log.L;

@SuppressWarnings("deprecation")
public class ShellCommand {
    private Boolean can_su;
    public SH sh;
    public SH su;

    public ShellCommand() {
        sh = new SH("sh");
        su = new SH("su");
    }

    public boolean canSU() {
        return canSU(false);
    }

    public boolean canSU(boolean force_check) {
        if (can_su == null || force_check) {
            CommandResult r = su.runWaitFor("id");
            StringBuilder out = new StringBuilder();

            if (r.stdout != null)
                out.append(r.stdout).append(" ; ");

            if (r.stderr != null)
                out.append(r.stderr);

            can_su = r.success();
        }

        return can_su;
    }

    public SH suOrSH() {
        return canSU() ? su : sh;
    }

    public class CommandResult {
        public final String stdout;
        public final String stderr;
        public final Integer exit_value;

        CommandResult(Integer exit_value_in, String stdout_in, String stderr_in) {
            exit_value = exit_value_in;
            stdout = stdout_in;
            stderr = stderr_in;
        }

        CommandResult(Integer exit_value_in) {
            this(exit_value_in, null, null);
        }

        public boolean success() {
            return exit_value != null && exit_value == 0;
        }

        @Override
        public String toString() {
            return "CommandResult stdout: " + stdout + " stderr: " + stderr + " exit_value: " + exit_value;
        }
        
    }

    public class SH {
        private String SHELL = "sh";

        public SH(String SHELL_in) {
            SHELL = SHELL_in;
        }

        public Process run(String s) {
            Process process = null;

            try {
                process = Runtime.getRuntime().exec(SHELL);
                DataOutputStream toProcess = new DataOutputStream(process.getOutputStream());
                toProcess.writeBytes("exec " + s + "\n");
                toProcess.flush();

            } catch (Exception e) {
                L.w(e);
                process = null;
            }

            return process;
        }

        private String getStreamLines(InputStream is) {
            String out = null;
            StringBuffer buffer = null;
            DataInputStream dis = new DataInputStream(is);

            try {
                if (dis.available() > 0) {
                    buffer = new StringBuffer(dis.readLine());

                    while (dis.available() > 0)
                        buffer.append("\n").append(dis.readLine());
                }

                dis.close();

            } catch (Exception e) {
                L.w(e);
            }

            if (buffer != null)
                out = buffer.toString();

            return out;
        }

        public CommandResult runWaitFor(String s) {
            Process process = run(s);
            Integer exit_value = null;
            String stdout = null;
            String stderr = null;

            if (process != null) {
                try {
                    exit_value = process.waitFor();
                    stdout = getStreamLines(process.getInputStream());
                    stderr = getStreamLines(process.getErrorStream());

                } catch (InterruptedException e) {
                    L.w(e);
                } catch (NullPointerException e) {
                    L.w(e);
                }
            }

            return new CommandResult(exit_value, stdout, stderr);
        }
        
        public CommandResult execCommand(String[] commands, boolean needResult) {
            int result = -1;
            if (commands == null || commands.length == 0) {
                return new CommandResult(result, null, null);
            }

            Process process = null;
            BufferedReader successResult = null;
            BufferedReader errorResult = null;
            StringBuilder successMsg = null;
            StringBuilder errorMsg = null;

            DataOutputStream os = null;
            try {
                process = Runtime.getRuntime().exec(SHELL);
                os = new DataOutputStream(process.getOutputStream());
                for (String command : commands) {
                    if (command == null) {
                        continue;
                    }

                    // donnot use os.writeBytes(commmand), avoid chinese charset error
                    os.write(command.getBytes());
                    os.writeBytes("\n");
                    os.flush();
                }
                os.writeBytes("exit\n");
                os.flush();

                result = process.waitFor();
                if (needResult) {
                 // get command result
                    successMsg = new StringBuilder();
                    errorMsg = new StringBuilder();
                    successResult = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    errorResult = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                    String s;
                    while ((s = successResult.readLine()) != null) {
                        successMsg.append(s);
                    }
                    while ((s = errorResult.readLine()) != null) {
                        errorMsg.append(s);
                    }
                    L.d("Shell# successMsg %s", successMsg);
                    L.d("Shell# errorMsg %s", errorMsg);
                }
                
            } catch (IOException e) {
                L.w(e);
            } catch (Exception e) {
                L.w(e);
            } finally {
                try {
                    if (os != null) {
                        os.close();
                    }
                    if (successResult != null) {
                        successResult.close();
                    }
                    if (errorResult != null) {
                        errorResult.close();
                    }
                } catch (IOException e) {
                    L.w(e);
                }

                if (process != null) {
                    process.destroy();
                }
            }
            return new CommandResult(result, successMsg == null ? null : successMsg.toString(), errorMsg == null ? null
                    : errorMsg.toString());
        }
        
    }
}
