package library.users;

import java.io.*;
import java.util.Date;

/**
 * Created by Marcio on 26/05/2015.
 */
public class UsersFile {
    private File usersFile;

    public UsersFile(String fileName) {
        // Verifica se o diret�rio de dados j� foi criado
        File folder = new File("data");
        if (!folder.exists())
            folder.mkdir(); // Cria o diret�rio caso n�o tenha sido criado

        // Cria o arquivo dentro do diret�rio de dados
        usersFile = new File(folder, fileName);
    }

    public void storeUser(User user) {
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(usersFile, true);
            PrintWriter output = new PrintWriter(fileWriter);

            // Converte os dados do usu�rio para string
            String firstName = user.getFirstName();
            String lastName = user.getLastName();
            String email = user.getEmail();
            String userId = Long.toString(user.getUserId());
            String type = user.getGroup();
            String date = null;
            String status;
            if (user.isBlocked()) {
                status = "BLOCKED";
                date = user.getUnblockDate().toString();
            }
            else status = "UNBLOCKED";

            // Escreve os dados do usu�rio no arquivo
            output.printf("%s,%s,%s,%s,%s,%s,%s\n", firstName, lastName, email, userId, type, status, date);

            output.close();
            fileWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public User searchID(String ID) {
        FileReader reader = null;
        User userFound = null;
        String line;
        try {
            reader = new FileReader(usersFile);
            BufferedReader input = new BufferedReader(reader);

            // Le linha por linha do arquivo at� o fim
            while ((line = input.readLine()) != null){
                // Quebra a linha em partes
                String[] fields = line.split(",");
                // Verifica se o campo user ID est� nesse registro
                if (fields[3].equals(ID)) {
                    System.out.println("AQUI\n");
                    // Cria um novo objeto de acordo com o registro lido
                    userFound = createUserByReg(fields);

                    input.close();
                    reader.close();
                    return userFound;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return userFound;
    }

    // Recebe uma string com os campos de um registro do arquivo de dados e cria o respectivo objeto
    private User createUserByReg(String[] fields) {
        String firstName = fields[0];
        String lastName = fields[1];
        String email = fields[2];
        long userId = Long.parseLong(fields[3]);
        boolean blocked = fields[5].equals("BLOCKED");
        Date date;
        if (blocked)
            date = new Date(fields[6]);
        else date = null;

        User newUser = null;

        switch(fields[4]) {
            case "TEACHER":
                newUser = new Teacher(firstName, lastName, email, userId, blocked, date);
                break;
            case "STUDENT":
                newUser = new Student(firstName, lastName, email, userId, blocked, date);
                break;
            case "COMMUNITY MEMBER":
                newUser = new CommunityMember(firstName, lastName, email, userId, blocked, date);
                break;
        }

        return newUser;
    }
 }
