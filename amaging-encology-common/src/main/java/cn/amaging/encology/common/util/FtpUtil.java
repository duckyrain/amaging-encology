package cn.amaging.encology.common.util;

import cn.amaging.encology.common.util.ftp.FileInfo;
import cn.amaging.encology.common.util.ftp.LoginInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * Created by DuQiyu on 2018/10/23 11:34.
 */
public class FtpUtil {

    private static final Logger logger = LoggerFactory.getLogger(FtpUtil.class);


    private static final String DEFAULT_LOCAL_FOLDER_PATH = "/tmp";
    /**
     *  客户端是windows，服务端是linux时，common-net提供的FTPClient存在一些兼容问题，这里统一用'/'来标识文件夹，
     *  不要使用{@link File}中的separator，原因是File.separator值在windows下是'\'，在linux下是'/'
     */
    private static final char SEPARATOR_CHAR = '/';

    private static final String SEPARATOR = "/";

    /**
     * 通用上传方法
     * FileInfo参数说明：
     *  localFolderPath - 本地待上传目录，不可为空
     *  serverFolderPath - 服务器目录，默认为当前FTP账号的根目录
     *  localFileName - 本地文件名，为空则上传文件夹下的所有文件
     *  serverFileName - 上传到服务器的文件名，localFileName不为空时有效，为空则与localFileName相同
     *  renameFileName - 上传完成后的文件重命名（文件上传时，为防止其他用户访问，通常先使用临时文件名，待文件上传完成后再进行重命名）
     *  localFileNames - 本地文件名（批量上传）
     *  serverFileNames - 批量上传到服务器的文件名，localFileNames不为空时有效，顺序需要与localFileNames一一对应
     *  renameFileNames - 上传完成后的文件重命名，顺序需要与serverFileNames一一对应
     *  blackFilterRegex - 黑名单正则，上传文件夹时有效，不上传文件名匹配的文件
     *  whiteFilterRegex - 白名单正则，上传文件夹时有效，只上传文件名匹配的文件，优先级高于黑名单正则
     *  childFolderFlag - 是否上传子文件夹标识，文件夹上传时有效，默认：不上传
     *  backupPath - 备份路径，上传完成后，将本地文件备份到指定目录，为空不备份
     *  deleteFlag - 是否删除标识，上传完成后，是否删除本地文件，默认：不删除
     * @param loginInfo
     * @param fileInfo
     * */
    public static void upload(LoginInfo loginInfo, FileInfo fileInfo) {
        if (null == loginInfo) {
            logger.error("LoginInfo cannot be null, break.");
            return;
        }
        if (null == fileInfo || StringUtils.isBlank(fileInfo.getLocalFolderPath())) {
            logger.error("FileInfo is null or the FileInfo.localFolderPath is null.");
            return;
        }
        File localFolder = new File(fileInfo.getLocalFolderPath());
        if (!localFolder.exists()) {
            logger.error("Local folder path:{} is null.", fileInfo.getLocalFolderPath());
            return;
        }
        FTPClient ftpClient = null;
        try {
            ftpClient = login(loginInfo);
            if (StringUtils.isBlank(fileInfo.getServerFolderPath())) {
                fileInfo.setServerFolderPath(SEPARATOR);
            }
            if (StringUtils.isNotBlank(fileInfo.getLocalFileName()) ||
                    (null != fileInfo.getLocalFileNames() && fileInfo.getLocalFileNames().length > 0)) {
                uploadFiles(ftpClient, fileInfo);
            } else {
                uploadFolder(ftpClient, fileInfo);
            }
        } catch (Exception e) {
            logger.error("upload error.", e);
        } finally {
            logout(ftpClient, loginInfo);
        }
    }

    /**
     * 通用下载方法
     * FileInfo参数说明：
     *  localFolderPath - 本地保存目录
     *  serverFolderPath - 服务器目录，默认为当前FTP账号的根目录
     *  localFileName - 下载到本地的文件名，serverFileName不为空时有效，为空则与serverFileName相同
     *  serverFileName - 服务器的文件名，为空则下载文件夹下的所有文件
     *  localFileNames - 下载到本地的文件名（批量下载），serverFileNames不为空时有效，顺序与serverFileNames一一对应
     *  serverFileNames - 服务器的文件名
     *  blackFilterRegex - 黑名单正则，下载文件夹时有效，不下载文件名匹配的文件
     *  whiteFilterRegex - 白名单正则，下载文件夹时有效，只下载文件名匹配的文件，优先级高于黑名单正则
     *  childFolderFlag - 是否下载子文件夹标识，下载文件夹时有效，默认：不下载
     *  backupPath - 备份路径，下载完成后，将服务器文件备份到指定目录，为空则不备份
     *  deleteFlag - 是否删除标识，下载完成后，是否删除服务器文件，默认：不删除
     * @param loginInfo
     * @param fileInfo
     * */
    public static void download(LoginInfo loginInfo, FileInfo fileInfo) {
        if (null == loginInfo) {
            logger.error("LoginInfo cannot be null, break.");
            return;
        }
        if (null == fileInfo) {
            fileInfo = new FileInfo();
        }
        FTPClient ftpClient = null;
        try {
            ftpClient = login(loginInfo);
            if (StringUtils.isBlank(fileInfo.getLocalFolderPath())) {
                fileInfo.setLocalFolderPath(DEFAULT_LOCAL_FOLDER_PATH);
            }
            if (StringUtils.isNotBlank(fileInfo.getServerFileName()) ||
                    (null != fileInfo.getServerFileNames() && fileInfo.getServerFileNames().length > 0)) {
                downloadFiles(ftpClient, fileInfo);
            } else {
                downloadFolder(ftpClient, fileInfo);
            }
        } catch (Exception e) {
            logger.error("Download error.", e);
        } finally {
            logout(ftpClient, loginInfo);
        }
    }

    private static FTPClient login(LoginInfo loginInfo) throws IOException {
        String hostName = loginInfo.getHostName();
        int port = loginInfo.getPort();
        String userName = loginInfo.getUserName();
        String password = loginInfo.getPassword();
        FTPClient ftpClient = new FTPClient();
        ftpClient.connect(hostName, port);
        ftpClient.login(userName, password);
        logger.info("Login ftp server. host:{}, port:{}, userName:{}.", hostName, port, userName);
        // 验证登录情况
        int replyCode = ftpClient.getReplyCode();
        if (!FTPReply.isPositiveCompletion(replyCode)) {
            throw new RuntimeException("Login failed, error code: " + replyCode);
        }
        // 设置为被动模式，避免被服务器防火墙拦截
        ftpClient.enterLocalPassiveMode();
        return ftpClient;
    }

    private static void logout(FTPClient ftpClient, LoginInfo loginInfo) {
        if (null == ftpClient) {
            return;
        }
        logger.info("Logout ftp server. host:{}, port:{}, userName:{}.", loginInfo.getHostName(), loginInfo.getPort(),
                loginInfo.getUserName());
        try {
            ftpClient.logout();
        } catch (IOException e) {
            logger.error("Logout error.", e);
        }
    }

    private static void uploadFiles(FTPClient ftpClient, FileInfo fileInfo) throws IOException {
        // 单文件上传
        if (StringUtils.isNotBlank(fileInfo.getLocalFileName())) {
            String localFilePath = getAbsolutePath(fileInfo.getLocalFolderPath(), fileInfo.getLocalFileName());
            File localFile = new File(localFilePath);
            if (localFile.exists()) {
                String serverFileName = fileInfo.getServerFileName();
                // 上传文件名默认与本地文件名一致
                if (StringUtils.isBlank(serverFileName)) {
                    serverFileName = fileInfo.getLocalFileName();
                }
                String serverFilePath = getAbsolutePath(fileInfo.getServerFolderPath(), serverFileName);
                String renameFilePath = null;
                if (StringUtils.isNotBlank(fileInfo.getRenameFileName())) {
                    renameFilePath = getAbsolutePath(fileInfo.getServerFolderPath(), fileInfo.getRenameFileName());
                }
                uploadFile(ftpClient, serverFilePath, localFile, renameFilePath, fileInfo.getBackupPath(), fileInfo.isDeleteFlag());
            } else {
                logger.error("Local file:{} not exists.", getAbsolutePath(fileInfo.getLocalFolderPath(), fileInfo.getLocalFileName()));
            }
        }
        // 批量文件上传
        String[] localFileNames = fileInfo.getLocalFileNames();
        if (null != localFileNames && localFileNames.length > 0) {
            String[] serverFileNames = fileInfo.getServerFileNames();
            if (null == serverFileNames || serverFileNames.length == 0) {
                serverFileNames = new String[localFileNames.length];
            }
            String[] renameFileNames = fileInfo.getRenameFileNames();
            if (null == renameFileNames || renameFileNames.length ==0) {
                renameFileNames = new String[localFileNames.length];
            }
            for (int i = 0; i < localFileNames.length; ++i) {
                String localFilePath = getAbsolutePath(fileInfo.getLocalFolderPath(), localFileNames[i]);
                File localFile = new File(localFilePath);
                if (localFile.exists()) {
                    // 未设置服务器文件名时，上传的文件与本地文件名一致
                    if (StringUtils.isBlank(serverFileNames[i])) {
                        serverFileNames[i] = localFileNames[i];
                    }
                    String serverFilePath = getAbsolutePath(fileInfo.getServerFolderPath(), serverFileNames[i]);
                    String renameFilePath = null;
                    if (StringUtils.isNotBlank(renameFileNames[i])) {
                        renameFilePath = getAbsolutePath(fileInfo.getServerFolderPath(), renameFileNames[i]);
                    }
                    uploadFile(ftpClient, serverFilePath, localFile, renameFilePath, fileInfo.getBackupPath(), fileInfo.isDeleteFlag());
                } else {
                    logger.error("Local file:{} not exists.", localFilePath);
                }
            }
        }
    }

    private static void uploadFolder(FTPClient ftpClient, FileInfo fileInfo) throws IOException {
        String localFolderPath = fileInfo.getLocalFolderPath();
        File localFolder = new File(localFolderPath);
        // 上层做了文件夹是否存在判断，这里不再判断
        File[] localFiles = localFolder.listFiles();
        if (null == localFiles || localFiles.length == 0) {
            return;
        }
        for (File localFile : localFiles) {
            if (allowUpload(localFile, fileInfo)) {
                fileInfo.setLocalFolderPath(localFolderPath);
                if (localFile.isDirectory()) {
                    // 将文件夹设置到子文件夹，用于后续递归下载
                    fileInfo.setLocalFolderPath(getAbsolutePath(fileInfo.getLocalFolderPath(), localFile.getName()));
                    uploadFolder(ftpClient, fileInfo);
                } else {
                    String serverFilePath = getAbsolutePath(fileInfo.getServerFolderPath(), localFile.getName());
                    uploadFile(ftpClient, serverFilePath, localFile, null, fileInfo.getBackupPath(), fileInfo.isDeleteFlag());
                }
            }
        }
    }

    /**
     * （批量）下载指定文件
     * @param ftpClient
     * @param fileInfo
     * */
    private static void downloadFiles(FTPClient ftpClient, FileInfo fileInfo) throws IOException {
        // 获取FTP目录下文件名称列表，用于判断待下载的文件是否存在
        FTPFile[] ftpFiles = ftpClient.listFiles(fileInfo.getServerFolderPath());
        // 单文件下载
        if (StringUtils.isNotBlank(fileInfo.getServerFileName())) {
            if (exists(ftpFiles, fileInfo.getServerFileName())) {
                String localFileName = fileInfo.getLocalFileName();
                // 保存的文件名默认与服务器文件名一致
                if (StringUtils.isBlank(localFileName)) {
                    localFileName = fileInfo.getServerFileName();
                }
                String serverFilePath = getAbsolutePath(fileInfo.getServerFolderPath(), fileInfo.getServerFileName());
                String localFilePath = getAbsolutePath(fileInfo.getLocalFolderPath(), localFileName);
                downloadFile(ftpClient, serverFilePath, localFilePath, fileInfo.getBackupPath(), fileInfo.isDeleteFlag());
            } else {
                logger.warn("FTP file:{} not exists.", getAbsolutePath(fileInfo.getServerFolderPath(), fileInfo.getServerFileName()));
            }
        }
        // 批量文件下载
        String[] serverFileNames = fileInfo.getServerFileNames();
        if (null != serverFileNames && serverFileNames.length > 0) {
            String[] localFileNames = fileInfo.getLocalFileNames();
            if (null == localFileNames || localFileNames.length == 0) {
                localFileNames = new String[serverFileNames.length];
            }
            for (int i = 0; i < serverFileNames.length; ++i) {
                if (exists(ftpFiles, serverFileNames[i])) {
                    // 未设置本地文件名时，下载的文件与服务器文件名一致
                    if (StringUtils.isBlank(localFileNames[i])) {
                        localFileNames[i] = serverFileNames[i];
                    }
                    String serverFilePath = getAbsolutePath(fileInfo.getServerFolderPath(), serverFileNames[i]);
                    String localFilePath = getAbsolutePath(fileInfo.getLocalFolderPath(), localFileNames[i]);
                    downloadFile(ftpClient, serverFilePath, localFilePath, fileInfo.getBackupPath(), fileInfo.isDeleteFlag());
                } else {
                    logger.warn("FTP file:{} not exists.", getAbsolutePath(fileInfo.getServerFolderPath(), serverFileNames[i]));
                }
            }
        }
    }

    /**
     * 下载目录的所有文件
     * @param ftpClient
     * @param fileInfo
     * */
    private static void downloadFolder(FTPClient ftpClient, FileInfo fileInfo) throws IOException {
        FTPFile[] ftpFiles = ftpClient.listFiles(fileInfo.getServerFolderPath());
        if (null == ftpFiles || ftpFiles.length == 0) {
            return;
        }
        String serverFolderPath = fileInfo.getServerFolderPath();
        for (FTPFile ftpFile : ftpFiles) {
            if (allowDownload(ftpFile, fileInfo)) {
                fileInfo.setServerFolderPath(serverFolderPath);
                if (ftpFile.isDirectory()) {
                    // 将文件夹设置到子文件夹，用于后续递归下载
                    fileInfo.setServerFolderPath(getAbsolutePath(fileInfo.getServerFolderPath(), ftpFile.getName()));
                    downloadFolder(ftpClient, fileInfo);
                } else {
                    String serverFilePath = getAbsolutePath(fileInfo.getServerFolderPath(), ftpFile.getName());
                    String localFilePath = getAbsolutePath(fileInfo.getLocalFolderPath(), ftpFile.getName());
                    downloadFile(ftpClient, serverFilePath, localFilePath, fileInfo.getBackupPath(), fileInfo.isDeleteFlag());
                }
            }
        }
    }

    /**
     * 本地文件localFilePath上传到服务器serverFilePath
     * @param ftpClient
     * @param localFile - 本地文件
     * @param serverFilePath - 服务器保存文件绝对路径
     * @param renameFilePath - 上传完成后对服务器文件重命名
     * @param backupPath - 本地备份路径
     * @param deleteFlag - 本地文件删除标识
     * */
    private static void uploadFile(FTPClient ftpClient, String serverFilePath, File localFile,
                                   String renameFilePath, String backupPath, boolean deleteFlag) {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(localFile);
            // FTP服务器文件不进行备份，直接覆盖
            logger.info("Upload local file. localFilePath:{}, serverFilePath:{}", localFile.getAbsolutePath(), serverFilePath);
            ftpClient.storeFile(serverFilePath, fileInputStream);
            if (StringUtils.isNotBlank(renameFilePath)) {
                logger.info("Rename server file. serverFilePath:{}, renameFilePath:{}", serverFilePath, renameFilePath);
                ftpClient.rename(serverFilePath, renameFilePath);
            }
        } catch (Exception e) {
            logger.error("Upload local file error.", e);
        } finally {
            if (null != fileInputStream) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (StringUtils.isNotBlank(backupPath)) {
            File backupFolder = new File(backupPath);
            if (!backupFolder.exists()) {
                backupFolder.mkdirs();
            }
            File backupFilePath = new File(getAbsolutePath(backupPath, localFile.getName()));
            logger.info("Move local file. localFilePath:{}, backupFilePath:{}", localFile.getAbsolutePath(), backupFilePath);
            localFile.renameTo(backupFilePath);
        } else if (deleteFlag) {
            logger.info("Delete local file. localFilePath:{}", localFile.getAbsolutePath());
            localFile.delete();
        }
    }

    /**
     * 服务器文件serverFilePath保存到本地localFilePath
     * @param ftpClient
     * @param serverFilePath - 服务器文件绝对路径
     * @param localFilePath - 本地保存文件绝对路径
     * @param backupPath - 服务器备份路径
     * @param deleteFlag - 服务器文件删除标识
     * */
    private static void downloadFile(FTPClient ftpClient, String serverFilePath, String localFilePath,
                                     String backupPath, boolean deleteFlag) {
        File localFile = new File(localFilePath);
        if (localFile.exists()) {
            // 原文件存在，则进行本地备份
            backup(localFile);
        } else if (!localFile.getParentFile().exists()) {
            localFile.getParentFile().mkdirs();
        }
        FileOutputStream fileOutputStream = null;
        try {
            String serverFileName = serverFilePath.substring(serverFilePath.lastIndexOf(SEPARATOR) + 1);
            fileOutputStream = new FileOutputStream(localFile);
            ftpClient.retrieveFile(serverFilePath, fileOutputStream);
            logger.info("Download server file. serverFilePath:{}, localFilePath:{}", serverFilePath, localFilePath);
            if (StringUtils.isNotBlank(backupPath)) {
                // 移动到备份目录
                String backupFilePath = getAbsolutePath(backupPath, serverFileName);
                logger.info("Move server file. serverFilePath:{}, backupFilePath:{}", serverFilePath, backupFilePath);
                ftpClient.rename(serverFilePath, backupFilePath);
            } else if (deleteFlag) {
                // 删除
                logger.info("Delete server file. serverFilePath:{}", serverFilePath);
                ftpClient.deleteFile(serverFilePath);
            }
        } catch (IOException e) {
            logger.error("Download server file error.", e);
        } finally {
            if (null != fileOutputStream) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static String getAbsolutePath(String folderPath, String fileName) {
        if (StringUtils.isBlank(folderPath)) {
            folderPath = SEPARATOR;
        }
        if (folderPath.charAt(folderPath.length() - 1) == SEPARATOR_CHAR) {
            return folderPath + fileName;
        } else {
            return folderPath + SEPARATOR + fileName;
        }
    }

    private static boolean exists(FTPFile[] ftpFiles, String serverFileName) {
        if (null == ftpFiles || ftpFiles.length == 0) {
            return false;
        }
        for (FTPFile ftpFile : ftpFiles) {
            if (ftpFile.getName().equalsIgnoreCase(serverFileName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断当前文件是否允许下载
     * ftpFile和fileInfo在上层已经做了非空判断，这里不再判断
     * */
    private static boolean allowDownload(FTPFile ftpFile, FileInfo fileInfo) {
        boolean result = true;
        if (StringUtils.isNotBlank(fileInfo.getWhiteFilterRegex())) {
            result = Pattern.matches(fileInfo.getWhiteFilterRegex(), ftpFile.getName());
        } else if (StringUtils.isNotBlank(fileInfo.getBlackFilterRegex())) {
            result = !Pattern.matches(fileInfo.getBlackFilterRegex(), ftpFile.getName());
        }
        // 1. 白名单、黑名单校验通过；2. 当前文件不是文件夹或者允许下载文件夹
        return result && (!ftpFile.isDirectory() || fileInfo.isDownloadFolderFlag());
    }

    /**
     * 判断当前文件是否允许上传
     * file和fileInfo在上层已经做了非空判断，这里不再判断
     * */
    private static boolean allowUpload(File file, FileInfo fileInfo) {
        boolean result = true;
        if (StringUtils.isNotBlank(fileInfo.getWhiteFilterRegex())) {
            result = Pattern.matches(fileInfo.getWhiteFilterRegex(), file.getName());
        } else if (StringUtils.isNotBlank(fileInfo.getBlackFilterRegex())) {
            result = !Pattern.matches(fileInfo.getBlackFilterRegex(), file.getName());
        }
        // 1. 白名单、黑名单校验通过；2. 当前文件不是文件夹或者允许上传文件夹
        return result && (!file.isDirectory() || fileInfo.isDownloadFolderFlag());
    }

    /**
     * 返回日期格式的字符串，用于对重复文件进行重命名备份
     * */
    private static boolean backup(File file) {
        if (null == file) {
            return true;
        }
        String DATA_FORMAT_BAK = ".yyyyMMddHHmmss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATA_FORMAT_BAK);
        simpleDateFormat.setLenient(false);
        String bakFilePath = file.getAbsolutePath() + simpleDateFormat.format(new Date());
        logger.info("Local file:{} exists, backup to {}.", file.getAbsolutePath(), bakFilePath);
        return file.renameTo(new File(bakFilePath));
    }

}
