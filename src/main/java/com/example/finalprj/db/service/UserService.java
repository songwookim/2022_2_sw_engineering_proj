package com.example.finalprj.db.service;

import com.example.finalprj.db.domain.Dog;
import com.example.finalprj.db.domain.Playground;
import com.example.finalprj.db.repository.PlaygroundRepository;
import com.example.finalprj.db.domain.Authority;
import com.example.finalprj.db.domain.User;
import com.example.finalprj.db.repository.UserRepository;
import com.example.finalprj.db.service.data.Body;
import com.example.finalprj.db.service.data.Item;
import com.example.finalprj.db.service.data.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final PlaygroundRepository playgroundRepository;

    public Dog get(String url) {  // ResponseEntity<String> get
        return Dog.builder()
                .dogNm("test name")
                .dogRegNo("12345")
                .kindNm("test kind")
                .sexNm("test sex")
                .build();

//        RestTemplate restTemplate = new RestTemplate() ;
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//        HttpEntity<HttpHeaders> entity = new HttpEntity<>(headers);
//
//        ResponseEntity<String> response = restTemplate.exchange(URI.create(url),
//                HttpMethod.GET, entity, String.class);
//
//        String xml = response.getBody();
//        Body xmlBody;
//        try {
//            xmlBody = parser(xml).getBody();
//        } catch (Exception e) {
//            return null;
//        }
//        if(xmlBody != null) {
//            Item item = xmlBody.item;
//            return Dog.builder()
//                    .dogNm(item.dogNm)
//                    .dogRegNo(item.dogRegNo)
//                    .kindNm(item.kindNm)
//                    .sexNm(item.sexNm)
//                    .build();
//        }

//        return null;
    }

    public Response parser(String xml) {
        ObjectMapper xmlMapper = new XmlMapper();
        Response response = null;
        try {
            response = xmlMapper.readValue(xml, Response.class);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return response;
    }


    public User save(User user) {
        return userRepository.save(user);
    }

    public void addAuthority(Long userId, String authority){
        userRepository.findById(userId).ifPresent(user-> {
            Authority newRole = new Authority(user.getId(), authority);
            if (user.getAuthorities() == null) {
                HashSet<Authority> authorities = new HashSet<>();
                authorities.add(newRole);
                user.setAuthorities(authorities);
                save(user);
            };
        });
    }

    public Optional<Playground> findPlaygroundByPgName(String name) {
        return playgroundRepository.findPlaygroundByPgName(name);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<User> findAllManager() {
        return userRepository.findAllByPlaygroundIsNotNull(); // 놀이터를 관리 id가 있으면
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }


    public Long findIdByEmail(String email) {

        return userRepository.findIdByEmail(email);
    }

    public String getJson(User user) {
        JSONObject json1 = new JSONObject();
        ArrayList<JSONObject> array = new ArrayList<>();

        JSONObject json = new JSONObject();
        json.put("createdAt", user.getCreatedAt().toString());
        json.put("updatedAt", user.getUpdatedAt().toString());
        json.put("id", user.getId());
        json.put("name", user.getName());
        json.put("email", user.getEmail());
        json.put("phone_num", user.getPhoneNumber());
        json.put("birth", user.getPhoneNumber());
        json.put("residence", user.getResidence());
        json.put("dog_num", user.getDogNum());
        json.put("dogNm", user.getDog().dogNm);
        json.put("kindNm", user.getDog().kindNm);
        json.put("sexNm", user.getDog().sexNm);

        array.add(json);
        json1.put("data", array);

        return json1.toString();
    }

}
