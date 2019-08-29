package com.project.bean;

/**
 * Created by xieyanhao on 16/9/25.
 */
public class LockUserBean {

    private int device_id;
    private int lock_id;
    private String user_name; // 用户名称
    private int unlock_times; // 开锁次数
    private String fingerprintId; // 指纹ID

    public int getDevice_id() {
        return device_id;
    }

    public void setDevice_id(int device_id) {
        this.device_id = device_id;
    }

    public int getLock_id() {
        return lock_id;
    }

    public void setLock_id(int lock_id) {
        this.lock_id = lock_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public int getUnlock_times() {
        return unlock_times;
    }

    public void setUnlock_times(int unlock_times) {
        this.unlock_times = unlock_times;
    }

    public String getFingerprintId() {
        return fingerprintId;
    }

    public void setFingerprintId(String fingerprintId) {
        this.fingerprintId = fingerprintId;
    }

    public static class UnlockPswBean {

        private int device_id;
        private int lock_id;
        private String unlock_psw; // 开锁密码
        private int unlock_times; // 开锁次数

        public int getDevice_id() {
            return device_id;
        }

        public void setDevice_id(int device_id) {
            this.device_id = device_id;
        }

        public int getLock_id() {
            return lock_id;
        }

        public void setLock_id(int lock_id) {
            this.lock_id = lock_id;
        }

        public String getUnlock_psw() {
            return unlock_psw;
        }

        public void setUnlock_psw(String unlock_psw) {
            this.unlock_psw = unlock_psw;
        }

        public int getUnlock_times() {
            return unlock_times;
        }

        public void setUnlock_times(int unlock_times) {
            this.unlock_times = unlock_times;
        }
    }
}
