package com.example.finalprj.db.service;


import com.example.finalprj.db.domain.Playground;
import com.example.finalprj.db.domain.User;
import com.example.finalprj.db.repository.PlaygroundRepository;
import com.example.finalprj.db.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.StringTokenizer;

@Service
@RequiredArgsConstructor
public class PlaygroundService {

    final private PlaygroundRepository playgroundRepository;
    final private UserRepository userRepository;

    String driverName = "com.mysql.cj.jdbc.Driver"; //org.gjt.mm.mysql.Driver
    String url = "jdbc:mysql://3.34.74.19:3306/songwoo"; //:3306
    String id = "root";
    String pwd = "1234";

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

    public ArrayList<Playground> getPlaygroundList() {
        ArrayList<Playground> list = new ArrayList<>();
        String SQL = "select * from playground order by id asc";
        try (
                Connection conn = dbConn();
                PreparedStatement pstmt = conn.prepareStatement(SQL);
                ResultSet rs = pstmt.executeQuery();
        ) {
            while (rs.next()) {
                Playground dto = Playground.builder()
                        .id(rs.getLong("id"))
                        .pgName(rs.getString("pg_name"))
                        .lat(rs.getString("lat"))
                        .lng(rs.getString("lng"))
                        .build();
                list.add(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public int playgroundInsert() throws IOException, ParserConfigurationException, SAXException, ClassNotFoundException, SQLException {
        deleteAll();
        ArrayList<Playground> playgroundArrayList = getPlaygroundListFromXML();
        int count = 0;
        String SQL = "";
        try {
            Connection conn = dbConn();
            for (Playground dto : playgroundArrayList) {
                System.out.println(dto.getPgName());
                SQL = String.format("insert into playground(pg_name, lat, lng) values(\"%s\",\"%s\",\"%s\");", dto.getPgName(), dto.getLat(), dto.getLng());

                PreparedStatement pstmt = conn.prepareStatement(SQL);
                count = pstmt.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dbConn().close();
        }
        updateIdx();
        return count;
    }


    public int deleteAll() {
        String SQL = "delete from playground";
        int count = 0;
        try (
                Connection conn = dbConn();
                PreparedStatement pstmt = conn.prepareStatement(SQL);
        ) {
            count = pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    public String json() {
        ArrayList<Playground> list = getPlaygroundList();

        int counter = list.size();
        JSONObject json1 = new JSONObject();
        ArrayList<JSONObject> array = new ArrayList<JSONObject>();

        if (counter >= 0) {
            for (Playground playground : list) {
                JSONObject json = new JSONObject();
                json.put("id", playground.getId());
                json.put("pg_name", playground.getPgName());
                json.put("lat", playground.getLat().replace("\\", ""));
                json.put("lng", playground.getLng());

                array.add(json);
            }
        }

        json1.put("data", array);

        return json1.toString();
    }

    public static ArrayList<Playground> getPlaygroundListFromXML() throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = factory.newDocumentBuilder();
        Document doc = documentBuilder.parse("http://ci2021playdang.dongyangmirae.kr/geo.xml");

        NodeList geoList = doc.getElementsByTagName("coordinates");
        NodeList nameList = doc.getElementsByTagName("name");

        ArrayList<Playground> playgroundArrayList = new ArrayList<>();

        for (int i = 0; i < nameList.getLength(); i++) {
            Node geo_node = (Node) geoList.item(i);

            String coordinate = geo_node.getTextContent();
            coordinate = coordinate.replaceAll(" ", "");

            StringTokenizer st = new StringTokenizer(coordinate, ",");
            String lat = st.nextToken();
            String lng = st.nextToken();

            Node name_node = (Node) nameList.item(i);
            String name = name_node.getTextContent();

            Playground playground = Playground.builder()
                    .id(1L)
                    .pgName(name)
                    .lat(lat)
                    .lng(lng)
                    .build();

            playgroundArrayList.add(playground);
        }
        return playgroundArrayList;
    }

    public void updateIdx() {
        String sql = "set @COUNT =0;";
        try {
            Connection conn = dbConn();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.executeUpdate();

            sql = "update playground set id = @COUNT:=@COUNT+1;";
            pstmt = conn.prepareStatement(sql);
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Playground> findAll() {
        return playgroundRepository.findAll();
    }

    public Optional<Playground> findById(long id) {
        return playgroundRepository.findById(id);
    }

    public Optional<Playground> findByPgName(String search) {
        return playgroundRepository.findPlaygroundByPgName(search);
    }
}
