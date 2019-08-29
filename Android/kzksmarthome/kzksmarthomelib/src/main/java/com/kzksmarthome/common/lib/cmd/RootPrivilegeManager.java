package com.kzksmarthome.common.lib.cmd;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import com.kzksmarthome.common.module.log.L;
import com.kzksmarthome.common.module.log.LogModuleName;

/**
 * This class is thread-safe, so it is safe to use it concurrently
 * 
 * @author panrq
 * @createDate 2015-3-10
 *
 */
public class RootPrivilegeManager {
    private static Process process;
    private final static String END_MARK = "Gjj_CMD_END";

    private static final Object Lock = new Object();

    public static Process ensureProcessExists(boolean requiresRootPrivilege) {
        try {
            if (process == null) {
                // start() sometimes hangs! see http://stackoverflow.com/questions/8688382/runtime-exec-bug-hangs-without-providing-a-process-object
                // and currently we have no way of working around it
                if (requiresRootPrivilege) {
                    process = new ProcessBuilder("su")
                    .redirectErrorStream(true)
                    .start();

                } else {
                    process = new ProcessBuilder("sh")
                    .redirectErrorStream(true)
                    .start();
                }

//                process = Runtime.getRuntime().exec("su");
            }

        } catch (Exception e) {
            L.w(e);
        }

        return process;
    }

    public static boolean requestRootPrivilege() {
        boolean result = false;
        String line = executeCommand("id\n" + "echo " + END_MARK + "\n", true);

        synchronized (Lock) {
            if (line != null && line.contains("uid=0(root)")) {
                result = true;

            } else if (process != null) {
                process.destroy();
                process = null;
            }
        }

        return result;
    }

    public static boolean silentInstallAPK(final String apkDestPath) {
        boolean result = false;
        StringBuilder cmdBuffer = new StringBuilder("export LD_LIBRARY_PATH=/vendor/lib:/system/lib\n")
        .append("pm install -r ").append(apkDestPath).append('\n')
        .append("echo ").append(END_MARK).append('\n');

        synchronized (Lock) {
            String line = executeCommand(cmdBuffer.toString(), true);

            if (line != null && line.toLowerCase().contains("success")) {
                result = true;

            } else if (process != null) {
                process.destroy();
                process = null;
            }
        }

        return result;
    }

    public static boolean silentUninstallPackage(final String pkgName) {
        boolean result = false;
        StringBuilder cmdBuffer = new StringBuilder("export LD_LIBRARY_PATH=/vendor/lib:/system/lib\n")
        .append("pm uninstall ").append(pkgName).append('\n')
        .append("echo ").append(END_MARK).append('\n');

        synchronized (Lock) {
            String line = executeCommand(cmdBuffer.toString(), true);

            if (line != null && line.toLowerCase().contains("success")) {
                result = true;

            } else if (process != null) {
                process.destroy();
                process = null;
            }
        }

        return result;
    }

    public static boolean isThisDeviceRooted() {
        boolean result = false;
        StringBuilder cmdBuffer = new StringBuilder("export LD_LIBRARY_PATH=/vendor/lib:/system/lib\n")
        .append("type su > /dev/null 2>&1; echo $?\n")
        .append("echo ").append(END_MARK).append('\n');

        synchronized (Lock) {
            try {
                String line = executeCommand(cmdBuffer.toString(), false);
                result = line != null && line.startsWith("0");

            } finally {
                if (process != null) {
                    process.destroy();
                    process = null;
                }
            }
        }

        L.d("%s root available: %b", LogModuleName.UTIL, result);

        return result;
    }

    public static String executeCommand(String cmd, boolean requiresRootPrivilege) {
        try {
            synchronized (Lock) {
                if (ensureProcessExists(requiresRootPrivilege) == null)
                    return null;

                OutputStream os = process.getOutputStream();
                os.write(cmd.getBytes());
                os.flush();
                StreamReader sr = new StreamReader(process.getInputStream());
                sr.start();
                Lock.wait();
                return sr.getOutputString();
            }

        } catch (Exception e) {
            L.w(e);
        }

        return null;
    }

    static class StreamReader extends Thread {
        private BufferedReader mReader;
        private String mOutputString;

        public StreamReader(InputStream is) {
            setName(this.getClass().getSimpleName());
            mReader = new BufferedReader(new InputStreamReader(is));
        }

        public String getOutputString() {
            return mOutputString;
        }

        public void run() {
            synchronized (Lock) {
                try {
                    StringBuilder buffer = new StringBuilder();
                    String line;

                    while ((line = mReader.readLine()) != null) {
                        if (END_MARK.equals(line))
                            break;

                        buffer.append(line).append('\n');
                    }

                    mOutputString = buffer.toString();

                } catch (Exception e) {
                    L.w(e);
                    mOutputString = null;

                } finally {
                    Lock.notify();
                }
            }
        }
    }
}
