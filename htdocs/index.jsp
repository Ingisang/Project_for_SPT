<%@ page contentType="text/html;charset=KSC5601"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.io.File" %>
<%@ page import="org.apache.commons.fileupload.servlet.ServletFileUpload"%>
<%@ page import="org.apache.commons.fileupload.disk.DiskFileItemFactory"%>
<%@ page import="org.apache.commons.fileupload.*"%>
<%@ page import="org.apache.commons.io.FilenameUtils"%>
 
<html><head> <title> Apache ServletFileUpload example </title></head>
<body>
<center><table>
 <tr><td><h1>파일 업로드 결과 </h1></td></tr>
 <tr><td>
<%
 boolean isMultipart = ServletFileUpload.isMultipartContent(request);
 if (!isMultipart) {
 }else {
    FileItemFactory factory = new DiskFileItemFactory();
    ServletFileUpload upload = new ServletFileUpload(factory);
    List items = null;
    try {
       items = upload.parseRequest(request);
    } catch (FileUploadException e) {
     out.println("에러 1: "+e);
    }
    Iterator itr = items.iterator();
    while (itr.hasNext()) {
      FileItem item = (FileItem) itr.next();
      if (item.isFormField()) { // 파일이 아닌 폼필드에 입력한 내용을 가져옴.
        if(item!=null && item.getFieldName().equals("name")) {
          String name = item.getString("KSC5601");//form field 안에 입력한 데이터를 가져옴
          out.println("전송자:"+name+"<br>"); 
        }else if(item!=null && item.getFieldName().equals("pwd")) {
          String desc = item.getString("KSC5601");
          out.println("파일에 대한 설명:"+desc+"<br>");
        }
     } else { // 폼 필드가 아니고 파일인 경우
    try {
       String itemName = item.getName();//로컬 시스템 상의 파일경로 및 파일 이름 포함
       if(itemName==null || itemName.equals("")) continue;
       String fileName = FilenameUtils.getName(itemName);// 경로없이 파일이름만 추출함
       // 전송된 파일을 서버에 저장하기 위한 절차
       //String rootPath = getServletContext().getRealPath("/");
       File savedFile = new File("/web/home/joongchoun/html/upload/storage/"+fileName); 
       item.write(savedFile);// 지정 경로에 파일을 저장함
     
       out.println("<tr><td><b>파일저장 경로:</b></td></tr><tr><td><b>"+savedFile+"</td></tr>");
       out.println("<tr><td><b><a href=\"DownloadServlet?file="+fileName+"\">"+fileName+"</a></td></tr>");
    } catch (Exception e) {
       out.println("서버에 파일 저장중 에러: "+e);
      }
   }
  }
 } 
%>
</table>
</center>
</body></html>