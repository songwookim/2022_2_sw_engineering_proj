package com.example.finalprj.api.dao;

import com.example.finalprj.api.dto.Trash;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;

import java.sql.*;
import java.util.ArrayList;

public class TrashDao {
    String driverName = "com.mysql.cj.jdbc.Driver"; //org.gjt.mm.mysql.Driver
    @Value("${db.url}")
    private String url ;
    @Value("${db.id}")
    private String id;

    @Value("${db.pwd}")
    private String pwd ;

    public Connection dbConn() throws ClassNotFoundException {
        Connection conn = null;
        try {
            Class.forName(driverName);
            conn = DriverManager.getConnection(url, id, pwd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

    public ArrayList<Trash> getTrashList() {
        ArrayList<Trash> list = new ArrayList<>();
        String SQL = "select * from trash order by id asc";
        try (
                Connection conn = dbConn();
                PreparedStatement pstmt = conn.prepareStatement(SQL);
                ResultSet rs = pstmt.executeQuery();
        ) {
            while (rs.next()) {
                Trash dto = Trash.builder()
                        .id(rs.getLong("id"))
                        .status((Integer) rs.getObject("status"))
                        .build();
                list.add(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public void updateTrash(Trash trash) {
        String SQL = "update trash set status = " + trash.getStatus() + " where id = " + trash.getId() + ";";
        try (
                Connection conn = dbConn();
                PreparedStatement pstmt = conn.prepareStatement(SQL);
        ) {
            pstmt.executeUpdate(  );
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

//    public int playgroundInsert() throws IOException, ParserConfigurationException, SAXException, ClassNotFoundException, SQLException {
//        deleteAll();
//        int count = 0;
//        String SQL = "";
//        try {
//            Connection conn = dbConn();
//            for (Trash dto : getTrashList()) {
//                SQL = String.format("insert into playground(pg_name, lat, lng) values(\"%s\",\"%s\",\"%s\");", dto.getPgName(), dto.getLat(), dto.getLng());
//
//                PreparedStatement pstmt = conn.prepareStatement(SQL);
//                count = pstmt.executeUpdate();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            dbConn().close();
//        }
//        updateIdx();
//        return count;
//    }


//    public int deleteAll() {
//        String SQL = "delete from playground";
//        int count = 0;
//        try (
//                Connection conn = dbConn();
//                PreparedStatement pstmt = conn.prepareStatement(SQL);
//        ) {
//            count = pstmt.executeUpdate();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return count;
//    }

    public String json() {
        ArrayList<Trash> list = getTrashList();

        int counter = list.size();
        JSONObject json1 = new JSONObject();
        ArrayList<JSONObject> array = new ArrayList<JSONObject>();

        if (counter >= 0) {
            for (Trash dto : list) {
                JSONObject json = new JSONObject();
                json.put("id", dto.getId());
                json.put("status", dto.getStatus());

                array.add(json);
            }
        }

        json1.put("data", array);

        return json1.toString();
    }

}
