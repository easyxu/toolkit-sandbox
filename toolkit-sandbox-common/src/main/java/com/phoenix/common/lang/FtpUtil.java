package com.phoenix.common.lang;

import java.io.*;
import sun.net.*;
import sun.net.ftp.FtpClient;
import java.util.List;
import java.util.ArrayList;
import java.util.StringTokenizer;

import com.phoenix.common.file.FileUtil;


@SuppressWarnings("unchecked")
public class FtpUtil {
//  private FtpClient ftpClient;
//  public FtpUtil() {
//  }
//
//  /**
//   * connectServer
//   * 连接ftp服务器
//   * @throws java.io.IOException
//   * @param path 文件夹，空代表根目录
//   * @param password 密码
//   * @param user   登陆用户
//   * @param server 服务器地址
//   */
//  //连接ftp服务器
//  @SuppressWarnings("static-access")
//public void connectServer(String server, String user, String password,
//                            String path) throws IOException {
//    connectServer(server, ftpClient.FTP_PORT, user, password, path);
//  }
//
//  public void connectServer(String server, int port, String user,
//                            String password,
//                            String path) throws IOException {
//    // server：FTP服务器的IP地址；user:登录FTP服务器的用户名
//    // password：登录FTP服务器的用户名的口令；path：FTP服务器上的路径
//    ftpClient = new FtpClient();
//    ftpClient.openServer(server, port);
//    ftpClient.login(user, password);
//    //path是ftp服务下主目录的子目录
//    if (path.length() != 0) {
//      ftpClient.cd(path);
//      //用2进制上传
//    }
//    ftpClient.binary();
//  }
//
//  /**
//   * upload
//   * 上传文件
//   * @throws java.lang.Exception
//   * @return -1 文件不存在
//   *          -2 文件内容为空
//   *          >0 成功上传，返回文件的大小
//   * @param newname 上传后的新文件名
//   * @param filename 上传的文件
//   */
//  //上传文件;并返回上传文件的信息
//  public long upload(String filename, InputStream in) throws Exception {
//    long result = 0;
//    TelnetOutputStream os = null;
//    try {
//      os = ftpClient.put(filename);
//      result = FileUtil.streamCopy(in, os);
//    }
//    finally {
//      if (os != null) {
//        os.close();
//      }
//    }
//    return result;
//  }
//
//  public long upload(String filename, String newname) throws Exception {
//    FileInputStream is = null;
//    try {
//      java.io.File file_in = new java.io.File(filename);
//      if (!file_in.exists()) {
//        return -1;
//      }
//      if (file_in.length() == 0) {
//        return -2;
//      }
//      is = new FileInputStream(file_in);
//      return upload(newname, is);
//    }
//    finally {
//      if (is != null) {
//        is.close();
//      }
//    }
//  }
//
//  /**
//   * upload
//   * @throws java.lang.Exception
//   * @return
//   * @param filename
//   */
//  public long upload(String filename) throws Exception {
//    String newname = "";
//    if (filename.indexOf("/") > -1) {
//      newname = filename.substring(filename.lastIndexOf("/") + 1);
//    }
//    else {
//      newname = filename;
//    }
//    return upload(filename, newname);
//  }
//
//  /**
//   *  download
//   *  从ftp下载文件到本地
//   * @throws java.lang.Exception
//   * @return
//   * @param newfilename 本地生成的文件名
//   * @param filename 服务器上的文件名
//   */
//  public long download(String filename, OutputStream os) throws Exception {
//    TelnetInputStream is = null;
//    try {
//      is = ftpClient.get(filename);
//      return FileUtil.streamCopy(is, os);
//    }
//    finally {
//      if (is != null) {
//        is.close();
//      }
//
//    }
//
//  }
//
//  public long download(String filename, String newfilename) throws Exception {
//    long result = 0;
//
//    FileOutputStream os = null;
//    try {
//      os = new FileOutputStream(newfilename);
//      return download(filename, os);
//    }
//    catch (IOException e) {
//      e.printStackTrace();
//    }
//    finally {
//      if (os != null) {
//        os.close();
//      }
//    }
//    return result;
//  }
//
//  public void delete(String filename) {
//    try {
//      ftpClient.sendServer("DELE " + filename + "\r\n");
//      ftpClient.readServerResponse();
//    }
//    catch (IOException ex) {
//    }
//  }
//
//
//private List readStream(InputStream is) throws IOException  {
//    List list = new ArrayList();
//    ByteArrayOutputStream os = new ByteArrayOutputStream(2048);
//    FileUtil.streamCopy(is, os);
//    byte[] data = os.toByteArray();
//    int pos = 0, posend;
//    for (int i = 0; i < data.length; i++) {
//      if ( (data[i] == '\n') && (pos != i)) {
//        posend = i - 1;
//        if (data[posend] == '\r') {
//          posend--;
//        }
//        String fileName = new String(data, pos, posend - pos + 1);
//        System.out.println(fileName);
//        list.add(fileName);
//        pos = i + 1;
//      }
//    }
//    return list;
//  }
//
//  public List list(String path) {
//      InputStream is = null;
//
//    try {
//      String pwd = ftpClient.pwd();
//      ftpClient.cd(path);
//      is = ftpClient.list();
//      List result= readStream(is);
//      ftpClient.cd(pwd);
//      return result;
//    }
//    catch (IOException ex) {
//    }
//   return null;
//  }
//
//  public List getFileList(String path) {
//    InputStream is = null;
//    try {
//      is = ftpClient.nameList(path);
//      return readStream(is);
//    }
//    catch (Exception e) {
//      // e.printStackTrace();
//    }
//    finally {
//      FileUtil.close(is);
//    }
//    return null;
//  }
//
//  /**
//   * closeServer
//   * 断开与ftp服务器的链接
//   * @throws java.io.IOException
//   */
//  public void closeServer() throws IOException {
//    try {
//      if (ftpClient != null) {
//        ftpClient.closeServer();
//      }
//    }
//    catch (IOException e) {
//      e.printStackTrace();
//    }
//  }
//
//  public boolean exists(String fileName) {
//    List list = getFileList(fileName);
//    return!list.isEmpty();
//  }
//
//  public void makeDir(String pathList) throws Exception {
//    ftpClient.ascii();
//    StringTokenizer s = new StringTokenizer(pathList, "/");
//    String pathName = "";
//    int reply;
//    while (s.hasMoreElements()) {
//      pathName = pathName + "/" + (String) s.nextElement();
//      try {
//        ftpClient.sendServer("XMKD " + pathName + "\r\n");
//      }
//      catch (Exception e) {
//      }
//      reply = ftpClient.readServerResponse();
//      System.out.println("ftpServer:" + reply);
//    }
//
//    ftpClient.binary();
//  }

}
