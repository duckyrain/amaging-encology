package cn.amaging.encology.common.util.ftp;

import java.util.Arrays;

/**
 * Created by DuQiyu on 2018/10/25 9:25.
 */
public class FileInfo {

    /**
     * 本地目录
     */
    private String localFolderPath;

    /**
     * 服务器目录
     */
    private String serverFolderPath;

    /**
     * 本地文件名
     */
    private String localFileName;

    /**
     * 服务器文件名
     */
    private String serverFileName;

    /**
     * 重命名文件名
     * */
    private String renameFileName;

    /**
     * 本地文件名
     * */
    private String[] localFileNames;

    /**
     * 服务器文件名
     * */
    private String[] serverFileNames;

    /**
     * 重命名文件名
     * */
    private String[] renameFileNames;

    /**
     * 黑名单正则
     */
    private String blackFilterRegex;

    /**
     * 白名单正则
     */
    private String whiteFilterRegex;

    /**
     * 下载文件夹标识
     */
    private boolean downloadFolderFlag;

    /**
     * 服务器备份目录(为空则不备份)
     */
    private String backupPath;

    /**
     * 是否删除标识
     */
    private boolean deleteFlag;

    public FileInfo() {
    }

    public String getLocalFolderPath() {
        return localFolderPath;
    }

    public void setLocalFolderPath(String localFolderPath) {
        this.localFolderPath = localFolderPath;
    }

    public String getServerFolderPath() {
        return serverFolderPath;
    }

    public void setServerFolderPath(String serverFolderPath) {
        this.serverFolderPath = serverFolderPath;
    }

    public String getLocalFileName() {
        return localFileName;
    }

    public void setLocalFileName(String localFileName) {
        this.localFileName = localFileName;
    }

    public String getServerFileName() {
        return serverFileName;
    }

    public void setServerFileName(String serverFileName) {
        this.serverFileName = serverFileName;
    }

    public String getBlackFilterRegex() {
        return blackFilterRegex;
    }

    public void setBlackFilterRegex(String blackFilterRegex) {
        this.blackFilterRegex = blackFilterRegex;
    }

    public String getWhiteFilterRegex() {
        return whiteFilterRegex;
    }

    public void setWhiteFilterRegex(String whiteFilterRegex) {
        this.whiteFilterRegex = whiteFilterRegex;
    }

    public boolean isDownloadFolderFlag() {
        return downloadFolderFlag;
    }

    public void setDownloadFolderFlag(boolean downloadFolderFlag) {
        this.downloadFolderFlag = downloadFolderFlag;
    }

    public String getBackupPath() {
        return backupPath;
    }

    public void setBackupPath(String backupPath) {
        this.backupPath = backupPath;
    }

    public boolean isDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(boolean deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public String[] getLocalFileNames() {
        return localFileNames;
    }

    public void setLocalFileNames(String[] localFileNames) {
        this.localFileNames = localFileNames;
    }

    public String[] getServerFileNames() {
        return serverFileNames;
    }

    public void setServerFileNames(String[] serverFileNames) {
        this.serverFileNames = serverFileNames;
    }

    public String getRenameFileName() {
        return renameFileName;
    }

    public void setRenameFileName(String renameFileName) {
        this.renameFileName = renameFileName;
    }

    public String[] getRenameFileNames() {
        return renameFileNames;
    }

    public void setRenameFileNames(String[] renameFileNames) {
        this.renameFileNames = renameFileNames;
    }

    @Override
    public String toString() {
        return "FileInfo{" +
                "localFolderPath='" + localFolderPath + '\'' +
                ", serverFolderPath='" + serverFolderPath + '\'' +
                ", localFileName='" + localFileName + '\'' +
                ", serverFileName='" + serverFileName + '\'' +
                ", renameFileName='" + renameFileName + '\'' +
                ", localFileNames=" + Arrays.toString(localFileNames) +
                ", serverFileNames=" + Arrays.toString(serverFileNames) +
                ", renameFileNames=" + Arrays.toString(renameFileNames) +
                ", blackFilterRegex='" + blackFilterRegex + '\'' +
                ", whiteFilterRegex='" + whiteFilterRegex + '\'' +
                ", downloadFolderFlag=" + downloadFolderFlag +
                ", backupPath='" + backupPath + '\'' +
                ", deleteFlag=" + deleteFlag +
                '}';
    }
}
