package com.example.finalprj.api.dao;

import com.example.finalprj.api.dto.Photo;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;

import java.sql.*;
import java.util.ArrayList;

public class PhotoDao {
    String driverName="com.mysql.cj.jdbc.Driver"; //org.gjt.mm.mysql.Driver
    @Value("${db.url}")
    private String url ;
    @Value("${db.id}")
    private String id;

    @Value("${db.pwd}")
    private String pwd ;
    Statement stmt = null;

    public Connection dbConn() throws ClassNotFoundException {
        Connection conn = null;
        try {
            Class.forName(driverName);
            conn  = DriverManager.getConnection(url,id,pwd);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

    public ArrayList<Photo> getPhotoList() {
        ArrayList<Photo> list = new ArrayList<>();
        String SQL = "select * from photo order by id asc";

        try(
                Connection conn = dbConn();
                PreparedStatement pstmt = conn.prepareStatement(SQL);
                ResultSet rs = pstmt.executeQuery();
        ) {
            while(rs.next()) {
                Photo dto = new Photo();
                //dto.setId(rs.getString("id"));
                dto.setFileName(rs.getString("file_name"));
                dto.setUrl(rs.getString("url"));

                list.add(dto);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public void photoInsert(Photo dto) {
        String SQL="insert into photo(file_name, url)"+" values(?, ?)";
        try(
                Connection conn = dbConn();
                PreparedStatement pstmt = conn.prepareStatement(SQL);
        ){
            //pstmt.setString(1, dto.getId());
            pstmt.setString(1, dto.getFileName());
            pstmt.setString(2, dto.getUrl());

            pstmt.executeUpdate();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void photoDelete() {
        String SQL="delete from photo "+" where id = 3";
        try(
                Connection conn = dbConn();
                PreparedStatement pstmt = conn.prepareStatement(SQL);
        ){
            pstmt.executeUpdate();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public String json() {
//        String json = Json.createObjectBuilder()
//                .add("key1", "value1")
//                .add("key2", "value2")
//                .add("key3", "value3")
//                .build()
//                .toString();

        PhotoDao dao = new PhotoDao();
        ArrayList<Photo> list = dao.getPhotoList();

        int counter = list.size();
        JSONObject json1 = new JSONObject();
        ArrayList<JSONObject> array = new ArrayList<JSONObject>();
        int i =0;

        if(counter >= 0) {
            for(Photo photo: list) {
                JSONObject json2 = new JSONObject();
                //json2.put("id", photo.getId());
                json2.put("url", photo.getUrl());
                json2.put("file_name", photo.getFileName());

                array.add(json2);
            }
        }
        json1.put("data", array);
        return json1.toString();
    }
}
