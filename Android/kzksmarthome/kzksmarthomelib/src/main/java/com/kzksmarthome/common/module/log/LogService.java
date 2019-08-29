package com.kzksmarthome.common.module.log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import android.annotation.SuppressLint;
import android.os.Environment;
import android.text.TextUtils;

import com.kzksmarthome.common.app.SmartHomeAppLib;
import com.kzksmarthome.common.lib.cmd.ShellCommand;
import com.kzksmarthome.common.lib.cmd.ShellCommand.CommandResult;
import com.kzksmarthome.common.lib.cmd.ShellCommand.SH;
import com.kzksmarthome.common.lib.util.Util;

/**
 * 日志服务，日志默认会存储在SDcar里如果没有SDcard会存储在内存中的安装目录下面。 1.本服务默认在SDcard中每天生成一个日志文件, 2.如果有SDCard的话会将之前内存中的文件拷贝到SDCard中 3.如果没有SDCard，在安装目录下只保存当前在写日志
 * 4.SDcard的装载卸载动作会在步骤2,3中切换 5.SDcard中的日志文件只保存7天
 * 
 * @author Administrator
 * 
 */
public class LogService {
    private static final String TAG = "LogService";
    private static final String preFileName = "logcat_";
    private static final int SDCARD_LOG_FILE_SAVE_DAYS = 1; // sd卡中日志文件的最多保存天数
    private static LogService logService = null;

    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");// 日志名称格式
    private Timer timer;

    /**
     * 获取LogService对象
     * 
     * @return
     */
    public synchronized static LogService getInstance() {
        if (logService == null) {
            logService = new LogService();
        }
        return logService;
    }

    /**
     * 启动日志收集
     */
    public void start() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (!createLogDir()) {// 判断保存日志目录创建是否成功，创建成功才开始日志收集
            return;
        }
        deleteSDcardExpiredLog();
        startLogcat();
        L.d("%s, LogService start", TAG);
    }

    /**
     * 压缩日志文件
     * 
     * @param sourceDir
     * @param zipFilePath
     * @return
     * @throws IOException
     */
    public static File doZip(String sourceDir, String zipFilePath) throws Exception {
        File file = new File(sourceDir);
        File zipFile = null;
        if (file.exists()) {
            zipFile = new File(zipFilePath);
        }
        ZipOutputStream zos = null;
        try {
            // 创建写出流操作
            if (zipFile != null) {
                OutputStream os = new FileOutputStream(zipFile);
                BufferedOutputStream bos = new BufferedOutputStream(os);
                zos = new ZipOutputStream(bos);

                String basePath = null;

                // 获取目录
                if (file.isDirectory()) {
                    basePath = file.getPath();
                } else {
                    basePath = file.getParent();
                }
                zipFile(file, basePath, zos);
            }
        } finally {
            if (zos != null) {
                zos.closeEntry();
                // zos.close();
            }
        }

        if (file.exists()) {
            file.delete();
        }

        return zipFile;
    }

    /**
     * 关闭由本程序开启的logcat进程： 根据用户名称杀死进程(如果是本程序进程开启的Logcat收集进程那么两者的USER一致) 如果不关闭会有多个进程读取logcat日志缓存信息写入日志文件
     * 
     * @param orgProcessList
     * @return
     */
    private void killLogcatProc(List<String> orgProcessList) {
        int myUid = android.os.Process.myUid() - 10000;
        String uid = "app_" + myUid;
        String uidNew = "u0_a" + myUid;
        int size = orgProcessList.size();
        for (int i = 1; i < size; i++) {
            String processInfo = orgProcessList.get(i);
            String[] proStr = processInfo.split(" ");
            // USER PID PPID VSIZE RSS WCHAN PC NAME
            // root 1 0 416 300 c00d4b28 0000cd5c S /init
            List<String> orgInfo = new ArrayList<String>();
            for (String str : proStr) {
                if (!"".equals(str)) {
                    orgInfo.add(str);
                }
            }
            if (orgInfo.size() == 9) {
                if ("logcat".equals(orgInfo.get(8))
                        && (uid.equals(orgInfo.get(0)) || uidNew.equals(orgInfo.get(0)))) {
                    android.os.Process.killProcess(Integer.parseInt(orgInfo.get(1)));
                }
            }
        }

    }

    /**
     * 运行PS命令得到进程信息
     * 
     * @return USER PID PPID VSIZE RSS WCHAN PC NAME root 1 0 416 300 c00d4b28 0000cd5c S /init
     */
    private List<String> getAllProcess() {
        try {
            SH sh = new ShellCommand().sh;
            CommandResult cr = sh.runWaitFor("ps");
            if (!TextUtils.isEmpty(cr.stdout)) {
                String[] lines = cr.stdout.split("\n");
                return Arrays.asList(lines);
            }
        } catch (Exception e) {
            L.e(e);
        }
        return null;
    }

    /**
     * @param source 源文件
     * @param basePath
     * @param zos
     */
    private static void zipFile(File source, String basePath, ZipOutputStream zos)
            throws IOException {
        File[] files = null;
        if (source.isDirectory()) {
            files = source.listFiles();
        } else {
            files = new File[1];
            files[0] = source;
        }

        InputStream is = null;
        String pathName;
        byte[] buf = new byte[1024];
        int length = 0;
        try {
            for (File file : files) {
                if (file.isDirectory()) {
                    pathName = file.getPath().substring(basePath.length() + 1) + "/";
                    zos.putNextEntry(new ZipEntry(pathName));
                    zipFile(file, basePath, zos);
                } else {
                    if (file.exists()) {
                        pathName = file.getPath().substring(basePath.length() + 1);
                        is = new FileInputStream(file);
                        BufferedInputStream bis = new BufferedInputStream(is);
                        zos.putNextEntry(new ZipEntry(pathName));
                        while ((length = bis.read(buf)) > 0) {
                            zos.write(buf, 0, length);
                        }
                    }
                }
            }
        } finally {
            if (is != null) {
                is.close();
            }
        }

    }

    /**
     * 开启Logcat进程
     */
    private void startLogcat() {
        if (!checkRunning()) {
            createLogCollector();
        }
    }

    /**
     * kill Logcat进程
     */
    public void killLogcatProc() {
        List<String> orgProcList = getAllProcess();
        if (orgProcList != null) {
            killLogcatProc(orgProcList);
        }

    }

    /**
     * 延时 kill Logcat进程
     */
    public void delaykill() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                killLogcatProc();
            }
        };
        if (timer == null) {
            timer = new Timer();
        }
        timer.schedule(task, 3000 * 60);
    }

    /**
     * 判断 Logcat是否开启
     * 
     * @return
     */
    private static boolean checkRunning() {
        SH sh = new ShellCommand().sh;
        CommandResult cr = sh.runWaitFor("ps");
        if (cr.success()) {
            if (cr.stdout != null) {
                String[] lines = cr.stdout.split("\n");
                if (lines != null) {
                    int myUid = android.os.Process.myUid() - 10000;
                    String uid = "app_" + myUid;
                    String uidNew = "u0_a" + myUid;
                    for (String line : lines) {
                        if (line != null && line.contains("logcat")
                                && (line.contains(uid) || line.contains(uidNew))) {
                            return true;
                        }
                    }
                }
            }

        }
        return false;
    }

    /**
     * 开始收集日志信息
     */
    public void createLogCollector() {
        String filePath = getLogPath();
        if (filePath == null) {
            return;
        }
        StringBuilder sb = Util.getThreadSafeStringBuilder();
        sb.append("logcat").append(" -f ").append(filePath).append(" -v").append(" time")
                .append(" *:V");
        SH sh = new ShellCommand().sh;
        sh.runWaitFor(sb.toString());
    }

    /**
     * 删除过期日志
     */
    private void deleteSDcardExpiredLog() {
        File file = SmartHomeAppLib.getInstance().getContext().getExternalFilesDir("logcat");
        if (file != null && file.isDirectory()) {
            File[] allFiles = file.listFiles();
            for (File logFile : allFiles) {
                String fileName = logFile.getName();
                if (fileName.endsWith("txt")) {
                    String createDateInfo = getFileNameWithoutExtension(fileName);
                    if (createDateInfo != null && canDeleteSDLog(createDateInfo)) {
                        logFile.delete();
                        L.d("%s, delete expired log success,the log path is:"
                                + logFile.getAbsolutePath(), TAG);
                    }
                }
            }
        }
    }

    /**
     * 根据当前的存储位置得到日志的绝对存储路径
     * 
     * @return
     */
    public String getLogPath() {
        createLogDir();
        String logFileName = preFileName + sdf.format(new Date()) + ".txt";// 日志文件名称
        File file = SmartHomeAppLib.getInstance().getContext().getExternalFilesDir("logcat");
        if (file != null) {
            return file.getPath() + File.separator + logFileName;
        }

        return null;
    }

    /**
     * 获取日志文件夹路径
     * 
     * @return
     */
    public String getLogDirPath() {
        File file = SmartHomeAppLib.getInstance().getContext().getExternalFilesDir("logcat");
        if (file != null) {
            return file.getPath();
        }
        return null;
    }

    /**
     * 创建日志目录
     */
    private boolean createLogDir() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File file = SmartHomeAppLib.getInstance().getContext().getExternalFilesDir("logcat");
            if (file != null) {
                if (file.isDirectory()) {
                    return true;
                }
                return file.mkdirs();
            }
        }
        return false;
    }

    /**
     * 判断sdcard上的日志文件是否可以删除
     * 
     * @param createDateStr
     * @return
     */
    public boolean canDeleteSDLog(String createDateStr) {
        if (createDateStr != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, -1 * SDCARD_LOG_FILE_SAVE_DAYS);// 删除1天之前日志
            Date expiredDate = calendar.getTime();
            try {
                Date createDate = sdf.parse(createDateStr);
                return createDate.before(expiredDate);
            } catch (ParseException e) {
                L.e(e);
            }
        }
        return false;
    }

    /**
     * 日志文件的日期
     * 
     * @param fileName
     * @return
     */
    private String getFileNameWithoutExtension(String fileName) {
        if (fileName != null && fileName.length() > preFileName.length() && fileName.contains(".")) {
            return fileName.substring(preFileName.length(), fileName.indexOf("."));
        } else {
            return null;
        }
    }
}
