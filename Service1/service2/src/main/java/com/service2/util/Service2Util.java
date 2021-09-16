package com.service2.util;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;

import javax.crypto.Cipher;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.security.Key;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class Service2Util {


    private static final String ALGO = "AES";
    private static final String FILE_PATH = "providePath/consumer.csv";
    private static final String FILE_PATH_XML = "providePath/consumer.xml";

    public static Object decryptData(String message, String key) {
        try {
            Key decodedKey = (Key) SerializationUtils.deserialize(Base64.getDecoder().decode(key));

            Cipher cipher = Cipher.getInstance(ALGO);
            cipher.init(Cipher.DECRYPT_MODE, decodedKey);
            byte[] pt = cipher.doFinal(Base64.getDecoder().decode(message));
            return SerializationUtils.deserialize(pt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void writeIntoCSV(UserDto userDto) {
        List<String[]> sb = readCSVFile();
        int max = 0;
        Optional<String[]> optionalStrings = sb.stream().max(Comparator.comparing(strings -> strings[0]));
        if(optionalStrings.isPresent()){
            max = Integer.parseInt(optionalStrings.get()[0])+1;
        }
        userDto.setId(max);
        File file = new File(FILE_PATH);

        try {
            FileWriter outputfile = new FileWriter(file);
            CSVWriter writer = new CSVWriter(outputfile);
            String[] data = userDto.toString().split("#");
            sb.add(data);
            writer.writeAll(sb);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writeIntoXMLFile(UserDto userDto) throws JAXBException {
        UserDtoXml userDtoXml = readXMLFile();
        Optional<UserDto> userDto1 = userDtoXml.getUserDtos().stream().max(Comparator.comparing(UserDto::getId));
        if (userDto1.isPresent()) {
            userDto.setId(userDto1.get().getId() + 1);
            userDtoXml.getUserDtos().add(userDto);
        } else {
            userDtoXml.getUserDtos().add(userDto);
        }

        try {
            File file = new File(FILE_PATH_XML);
            JAXBContext jaxbContext = JAXBContext.newInstance(UserDtoXml.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.marshal(userDtoXml, file);

        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    public UserDtoXml readXMLFile() throws JAXBException {
        UserDtoXml userDtoList = new UserDtoXml();
        File file = new File(FILE_PATH_XML);
        if (file.length() != 0) {
            JAXBContext jaxbContext = JAXBContext.newInstance(UserDtoXml.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            try {
                userDtoList = (UserDtoXml) jaxbUnmarshaller.unmarshal(file);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return userDtoList;
    }

    public void updateXMLFile(UserDto userDto) throws JAXBException {
        UserDtoXml userDtoXml = readXMLFile();
        for (UserDto userDto1 : userDtoXml.getUserDtos()) {
            if (userDto1.getId() == userDto.getId()) {
                userDto1.setSalary(userDto.getSalary());
                userDto1.setAge(userDto.getAge());
                userDto1.setDob(userDto.getDob());
                userDto1.setName(userDto.getName());
            }
        }
        try {
            File file = new File(FILE_PATH_XML);
            JAXBContext jaxbContext = JAXBContext.newInstance(UserDtoXml.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.marshal(userDtoXml, file);

        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    public void updateUserCSV(UserDto userDto) {
        List<String[]> sb = readCSVFile();
        int index = -1;
        for (String[] str : sb) {
            if (str[0].equals(String.valueOf(userDto.getId()))) {
                index = sb.indexOf(str);
            }
        }
        if (index >= 0) {
            sb.remove(index);
            sb.add(index, userDto.toString().split("#"));
        }
        File file = new File(FILE_PATH);

        try {
            FileWriter outputfile = new FileWriter(file);
            CSVWriter writer = new CSVWriter(outputfile);
            writer.writeAll(sb);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<String[]> readCSVFile() {
        try {
            FileReader filereader = new FileReader(FILE_PATH);
            CSVReader csvReader = new CSVReaderBuilder(filereader).build();
            return csvReader.readAll();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean findDataById(String[] arr, String id) {
        return Arrays.asList(arr).contains(id);
    }

    public Date covertDate(String dateString) {
        Date date = new Date();
        try {
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            date = formatter.parse(dateString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    public List<UserDto> getUserData(List<String[]> list) {
        List<UserDto> userDtos = new ArrayList<>();
        list.forEach(e -> {
            UserDto userDto = new UserDto();
            userDto.setId(Integer.parseInt(e[0]));
            userDto.setName(e[1]);
            userDto.setDob(covertDate(e[2]));
            userDto.setAge(Integer.parseInt(e[3]));
            userDto.setSalary(Long.parseLong(e[4]));
            userDtos.add(userDto);
        });
        return userDtos;
    }

    public List<UserDto> getDataById(String id, String fileType) throws JAXBException {


        if (fileType.equalsIgnoreCase("csv")) {
            List<String[]> listCsv = readCSVFile();
            if (id.equalsIgnoreCase("ALL")) {
                return getUserData(listCsv);
            } else {
                return getUserData(listCsv.stream().filter(e -> findDataById(e, id)).collect(Collectors.toList()));
            }

        } else if (fileType.equalsIgnoreCase("xml")) {
            List<UserDto> listXml = readXMLFile().getUserDtos();
            if (id.equalsIgnoreCase("ALL")) {
                return listXml;
            } else {
                return listXml.stream().filter(e -> e.getId() == Integer.parseInt(id)).collect(Collectors.toList());
            }

        }
        return null;
    }
}


