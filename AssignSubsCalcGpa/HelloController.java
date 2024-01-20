package com.example.gpa_calculator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.sql.*;
import java.io.*;
import java.util.*;
import java.io.IOException;
import java.net.URI;
import  java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
public class HelloController implements Initializable {
//----------Pans----------\\
    @FXML
    private AnchorPane forgetpass_pane2;

    @FXML
    private AnchorPane signup_pane;

    @FXML
    private AnchorPane forgetpass_pane1;

    @FXML
    private AnchorPane subs_pane;
    @FXML
    private AnchorPane gpa_pane;
    @FXML
    private AnchorPane Grade4_pane;
    @FXML
    private AnchorPane signin_pane;


// ----------Buttons----------\\
    @FXML
    private Button END_GPA;
    @FXML
    private Button gotologin_btn;
    @FXML
    private Button next_gpa_btn;
    @FXML
    private Button exit_gpa_btn;
    @FXML
    private Button back_btn_fr2;
    @FXML
    private Button END_grade4;
    @FXML
    private Button back_btn_fr1;
    @FXML
    private Button loginacc_btn;
    @FXML
    private Button createacc_btn;


//----------CheckBox & ComboBox & hyperlink ----------\\
    @FXML
    private Hyperlink forgetpass_signin;
    @FXML
    private ComboBox<?> selectquestion_forgetpass;
    @FXML
    private ComboBox<?> selectquestion_signup;
    @FXML
    private CheckBox showpass_signin;


//----------Fields----------\\
    @FXML
    private PasswordField pass_forgetpass2;
    @FXML
    private TextField semster_signup;
    @FXML
    private TextField username_signup;
    @FXML
    private TextField email_forgetpass;
    @FXML
    private TextField level_signup;
    @FXML
    private TextField email_signup;
    @FXML
    private TextField email_signin;
    @FXML
    private PasswordField pass_signup;
    @FXML
    private PasswordField confpass_forgetpass2;
    @FXML
    private TextField answer_forgetpass;
    @FXML
    private PasswordField pass_signin;
    @FXML
    private TextField answer_signup;
    @FXML
    private TextField passtxt_signin;
    @FXML
    private TextField sub1_take;
    @FXML
    private TextField sub2_take;
    @FXML
    private TextField sub3_take;
    @FXML
    private TextField sub4_take;
    @FXML
    private TextField sub5_take;


//----------Labels----------\\
    @FXML
    private Label gpa_1;
    @FXML
    private Label gpa_2;
    @FXML
    private Label sub_1;
    @FXML
    private Label sub_2;
    @FXML
    private Label sub_3;
    @FXML
    private Label sub_4;
    @FXML
    private Label sub_5;
    @FXML
    private Label lbl_1;
    @FXML
    private Label lbl_2;
    @FXML
    private Label lbl_3;
    @FXML
    private Label lbl_4;
    @FXML
    private Label lbl_5;
    @FXML
    private Label lbl2_4grade;
    @FXML
    private Label grade4_gpa1;
    @FXML
    private Label grade4_gpa2;


//--DataBase Connection Variables----------\\
    private Connection connect ;
    private PreparedStatement pst;
    private ResultSet rs;
    private Statement st;


//----------Variables used inside projec----------\\
    String cur_email;
    String cur_lvl;
    String cur_semster;
    String cur_username;
    String cur_sub1;
    String cur_sub2;
    String cur_sub3;
    String cur_sub4;
    String cur_sub5;
    String cur_gpa1 = "A";
    String cur_gpa2 = "3.7";
    int level_after, semster_after;
    private static int idd = 1;





//----------Start Of The Project----------\\

    //----------Standard Function To Connect To DataBase----------\\
    public Connection connectDB(){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection connect =
                    DriverManager.getConnection("jdbc:mysql://localhost/gpa", "root", "");
            return connect;
        }catch (Exception ex){ex.printStackTrace();}
        return null;
    }

    //----------LOGIN PANE----------\\

    public void login(){
        alertMessage alert = new alertMessage();
        if(email_signin.getText().isEmpty() || pass_signin.getText().isEmpty()){
            alert.errorMessage("You Should Fill All Fields");
        }
        else{
            String query = "SELECT * FROM signup WHERE " + "email = ? and password = ?";
            connect = connectDB();
            if(showpass_signin.isSelected()){
                pass_signin.setText(passtxt_signin.getText());
                passtxt_signin.setText(pass_signin.getText());
            }
            try{
                pst = connect.prepareStatement(query);
                pst.setString(1,email_signin.getText());
                pst.setString(2,pass_signin.getText());
                rs = pst.executeQuery();
                if(rs.next()){
                    alert.successMessage("Login Successfully !");
                    putGrades(rs.getString(2),rs.getString(5), rs.getString(6), rs.getString(4));
                    signup_pane.setVisible(false);
                    forgetpass_pane1.setVisible(false);
                    forgetpass_pane2.setVisible(false);
                    signin_pane.setVisible(false);
                    subs_pane.setVisible(false);
                    if(check_exist()){
                        makeChanges();
                        subs_pane.setVisible(true);
                    }
                    else{
                        gpa_pane.setVisible(true);
                        assignInformations();
                        putGradesInformation();
                    }
//                    StoreFromDB();
                }
                else{
                    alert.errorMessage("Incorrect Email Or Password");
                }
            }catch(Exception ex){ex.printStackTrace();}


        }
    }


    //----------Functions Used Inside LOGIN Pane----------\\
    //1- get email & level & semster & username from database and assign it to global variables which we will use
    public void putGrades(String em, String lv, String smstr, String usrname){
        cur_email = em;
        cur_lvl = lv;
        cur_semster = smstr;
        cur_username = usrname;
    }


    // 2-make global variables contains subjects for the user who go to gpa page to put grades of his subjects
    public void assignInformations(){
        alertMessage alert = new alertMessage();
        String query = "SELECT * FROM subs WHERE " + "lvl = ? and semster = ?";
        connect = connectDB();
        try{
            pst = connect.prepareStatement(query);
            pst.setString(1,cur_lvl);
            pst.setString(2,cur_semster);
            rs = pst.executeQuery();
            if(rs.next()){
                alert.successMessage("That's your subjects type your grades");
                cur_sub1 = rs.getString(3);
                cur_sub2 = rs.getString(4);
                cur_sub3 = rs.getString(5);
                cur_sub4 = rs.getString(6);
                cur_sub5 = rs.getString(7);
            }
            else{
                alert.errorMessage("Error Happen While Connect To Data Base");
            }
        }catch(Exception ex){ex.printStackTrace();}

    }

    //3- assign these global variables that contain subjects as prompt text in textfield that we will take grades
    public void putGradesInformation(){
        sub1_take.setPromptText(cur_sub1);
        sub2_take.setPromptText(cur_sub2);
        sub3_take.setPromptText(cur_sub3);
        sub4_take.setPromptText(cur_sub4);
        sub5_take.setPromptText(cur_sub5);
    }


    //4-check if the user login calculate it's gpa before or calculate new one
    public boolean check_exist(){
        alertMessage alert = new alertMessage();
        String query = "SELECT * FROM studentsnext WHERE " + "email = ?";
        connect = connectDB();
        try{
            pst = connect.prepareStatement(query);
            pst.setString(1,email_signin.getText());
            rs = pst.executeQuery();
            if(rs.next()){
                return true;
            }
        }catch(Exception ex){ex.printStackTrace();}
        return false;
    }


    //5-assign informations for grades page from database when user login
    public void makeChanges(){
        alertMessage alert = new alertMessage();
        String query = "SELECT * FROM studentsnext WHERE " + "email = ?";
        connect = connectDB();
        try{
            pst = connect.prepareStatement(query);
            pst.setString(1,email_signin.getText());
            rs = pst.executeQuery();
            if(rs.next()){
                gpa_1.setText(rs.getString(4));
                gpa_2.setText(rs.getString(5));
                sub_1.setText(rs.getString(6));
                sub_2.setText(rs.getString(7));
                sub_3.setText(rs.getString(8));
                sub_4.setText(rs.getString(9));
                sub_5.setText(rs.getString(10));
                lbl_1.setText(rs.getString(11));
                lbl_2.setText(rs.getString(12));
                lbl_3.setText(rs.getString(13));
                lbl_4.setText(rs.getString(14));
                lbl_5.setText(rs.getString(15));
            }
            else{
                alert.errorMessage("Incorrect Email Or Password");
            }
        }catch(Exception ex){ex.printStackTrace();}
    }


    //----------REGISTER PANE----------\\
    public void register(){
        alertMessage alert = new alertMessage();
        if(email_signup.getText().isEmpty() || pass_signup.getText().isEmpty()
                || username_signup.getText().isEmpty() || level_signup.getText().isEmpty()
                || semster_signup.getText().isEmpty() || selectquestion_signup.getSelectionModel().getSelectedItem() == null
                ||answer_signup.getText().isEmpty()){
            alert.errorMessage("You Should Fill All Fields");
        }
        else if(pass_signup.getText().length() < 7) {
            alert.errorMessage("Invalid Password, at least 7 chracters needed");
        }
        else if(check_valid(level_signup.getText()) || check_valid(semster_signup.getText())){
            alert.errorMessage("level & semster should be numbers");
        }
        else if(Integer.parseInt(level_signup.getText()) > 4 || Integer.parseInt(level_signup.getText()) < 1){
            alert.errorMessage("level should be between 1 : 4");
        }
        else if(Integer.parseInt(semster_signup.getText()) > 2 || Integer.parseInt(semster_signup.getText()) < 1){
            alert.errorMessage("semster should be between 1 : 2");
        }
        else{
            String Query = "SELECT * FROM signup WHERE email = '" + email_signup.getText() + "'";
            connect = connectDB();

            try{
                st = connect.createStatement();
                rs = st.executeQuery(Query);
                if(rs.next()){
                    alert.errorMessage(email_signup.getText() + "Is Alerady Exist");
                }
                else{
                    Date date = new Date();
                    java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                    String query = "INSERT INTO signup " + "(user_id, email, password, username, lvl, semster, question, answer, idate) "
                            + "VALUES(?,?, ?, ?, ?, ?, ?, ?, ?)";
                    pst = connect.prepareStatement(query);
                    pst.setInt(1, idd); idd++;
                    pst.setString(2, email_signup.getText());
                    pst.setString(3, pass_signup.getText());
                    pst.setString(4, username_signup.getText());
                    pst.setInt(5, Integer.parseInt(level_signup.getText()));
                    pst.setInt(6, Integer.parseInt(semster_signup.getText()));
                    pst.setString(7,(String) selectquestion_signup.getSelectionModel().getSelectedItem());
                    pst.setString(8,answer_signup.getText());
                    pst.setString(9,String.valueOf(sqlDate));
                    pst.executeUpdate();
                    alert.successMessage("Registered Completed !");
                    register_clear();
                    signin_pane.setVisible(true);
                    signup_pane.setVisible(false);
                }
            }catch(Exception ex){ex.printStackTrace();}

        }
    }

    //----------Functions Used Inside Register Pane----------\\
    //1- clear the register fields after signup
    public void register_clear(){
        email_signup.setText("");
        pass_signup.setText("");
        username_signup.setText("");
        level_signup.setText("");
        semster_signup.setText("");
        answer_signup.setText("");
        selectquestion_signup.getSelectionModel().clearSelection();
    }


    //----------Forget Password PANE----------\\
    public void forgetPassword(){
        alertMessage alert = new alertMessage();
        if(email_forgetpass.getText().isEmpty() || answer_forgetpass.getText().isEmpty()
                || selectquestion_forgetpass.getSelectionModel().getSelectedItem() == null){
            alert.errorMessage("You Should Fill All Fields");
        }
        else{
            String query = "SELECT email, question, answer FROM signup WHERE " + "email = ? and question = ? and answer = ?";
            connect = connectDB();
            try{
                pst = connect.prepareStatement(query);
                pst.setString(1,email_forgetpass.getText());
                pst.setString(2,(String) selectquestion_forgetpass.getSelectionModel().getSelectedItem());
                pst.setString(3, answer_forgetpass.getText());
                rs = pst.executeQuery();
                if(rs.next()){
                    signup_pane.setVisible(false);
                    signin_pane.setVisible(false);
                    forgetpass_pane1.setVisible(false);
                    forgetpass_pane2.setVisible(true);                }
                else{
                    alert.errorMessage("Incorrect Informations Provided");
                }
            }catch(Exception ex){ex.printStackTrace();}


        }
    }


    //----------Change Password PANE----------\\
    public void changePassword() {
        alertMessage alert = new alertMessage();
        if (pass_forgetpass2.getText().isEmpty() || confpass_forgetpass2.getText().isEmpty()) {
            alert.errorMessage("You Should Fill All Fields");
        } else if (!(pass_forgetpass2.getText().equals(confpass_forgetpass2.getText()))) {
            alert.errorMessage("Passwords doesn't match");
        } else if (pass_forgetpass2.getText().length() < 7) {
            alert.errorMessage("Invalid Password, at least 7 characters");
        } else {
            String query = "UPDATE signup SET password = ?, update_date = ? " +
                    "WHERE email = '" + email_forgetpass.getText() + "'";
            connect = connectDB();
            try {
                Date date = new Date();
                java.sql.Date sqldate = new java.sql.Date(date.getTime());
                pst = connect.prepareStatement(query);
                pst.setString(1, pass_forgetpass2.getText());
                pst.setString(2, String.valueOf(sqldate));
                pst.executeUpdate();
                alert.successMessage("Successfully Changed your Password !");
                signup_pane.setVisible(false);
                forgetpass_pane1.setVisible(false);
                forgetpass_pane2.setVisible(false);
                signin_pane.setVisible(true);

                pass_forgetpass2.setText("");
                confpass_forgetpass2.setText("");
                email_signin.setText("");
                pass_signin.setText("");
                passtxt_signin.setVisible(false);
                pass_signin.setVisible(true);
                showpass_signin.setSelected(false);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }


    //----------GPA PANE----------\\
    public void put_gpa() {
        alertMessage alert = new alertMessage();
        if (sub1_take.getText().isEmpty() || sub2_take.getText().isEmpty()
                || sub3_take.getText().isEmpty() || sub4_take.getText().isEmpty()
                || sub5_take.getText().isEmpty()) {
            alert.errorMessage("You Should Fill All Fields");
        } else if (check_valid(sub1_take.getText()) || check_valid(sub2_take.getText())
                || check_valid(sub3_take.getText()) || check_valid(sub4_take.getText())
                || check_valid(sub5_take.getText())) {
            alert.errorMessage("You Should Enter Only Numbers" + sub1_take.getText() + sub2_take.getText());
        } else if (check_valid_number(sub1_take.getText()) || check_valid_number(sub2_take.getText())
                || check_valid_number(sub3_take.getText()) || check_valid_number(sub4_take.getText())
                || check_valid_number(sub5_take.getText())) {
            alert.errorMessage("Numbers Should Be Between Only 0 : 100 ");
        } else {
            String query = "INSERT INTO students " + "(email, lvl, semster, username, sub1, sub2, sub3, sub4, sub5, gpa1, gpa2) "
                    + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            connect = connectDB();

            try {
                pst = connect.prepareStatement(query);
                pst.setString(1, cur_email);
                pst.setInt(2, Integer.parseInt(cur_lvl));
                pst.setInt(3, Integer.parseInt(cur_semster));
                pst.setString(4, cur_username);
                pst.setInt(5, Integer.parseInt(sub1_take.getText()));
                pst.setInt(6, Integer.parseInt(sub2_take.getText()));
                pst.setInt(7, Integer.parseInt(sub3_take.getText()));
                pst.setInt(8, Integer.parseInt(sub4_take.getText()));
                pst.setInt(9, Integer.parseInt(sub5_take.getText()));
                calcGpa1();
                calcGpa2();
                cur_gpa2 = cur_gpa2.substring(0,3);
                pst.setString(10, cur_gpa1);
                pst.setString(11, cur_gpa2);
                pst.executeUpdate();
                signup_pane.setVisible(false);
                forgetpass_pane1.setVisible(false);
                forgetpass_pane2.setVisible(false);
                signin_pane.setVisible(false);
                subs_pane.setVisible(false);
                gpa_pane.setVisible(false);
                if(Integer.parseInt(cur_lvl) == 4 && Integer.parseInt(cur_semster) == 2){
                    nextSubject();
                }
                else{
                    nextSubject();
                    subs_pane.setVisible(true);
                }
            } catch (Exception ex) { ex.printStackTrace();}
        }
    }


    //----------Functions Used Inside GPA Pane----------\\
    //1-check if a grades that user types is only digits
    public static boolean check_valid(String s){
        for(int i = 0; i < s.length(); i++){
            char c = s.charAt(i);
            if(!Character.isDigit(c)){
                return true;
            }
        }
        return false;
    }


    //2-check if a grades that user types is between 0 : 100
    public boolean check_valid_number(String s){
        int x = Integer.parseInt(s);
        if(x >= 0 && x <= 100){
            return false;
        }
        return true;
    }


    //3- calculate gpa by call an API
    public void calcGpa1() throws IOException, InterruptedException {
        int sum = Integer.parseInt(sub1_take.getText()) + Integer.parseInt(sub2_take.getText()) + Integer.parseInt(sub3_take.getText())
                + Integer.parseInt(sub4_take.getText()) + Integer.parseInt(sub5_take.getText());
        sum /= 5;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://grade-calculator.p.rapidapi.com/grade/85/75/65/50/" + String.valueOf(sum)))
                .header("X-RapidAPI-Key", "8612ad9bb9mshd19309f7b55ce61p19fc5ejsn8130200415fd")
                .header("X-RapidAPI-Host", "grade-calculator.p.rapidapi.com")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        cur_gpa1 = response.body();
    }

    //4- Calculate numerical GPA by threads
    public void calcGpa2(){
        int g1 = Integer.parseInt(sub1_take.getText());
        int g2 = Integer.parseInt(sub2_take.getText());
        int g3 = Integer.parseInt(sub3_take.getText());
        int g4 = Integer.parseInt(sub4_take.getText());
        int g5 = Integer.parseInt(sub5_take.getText());
        gpaCalculator gc = new gpaCalculator(g1, g2, g3, g4, g5);
        Thread thread = new Thread(gc);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Double ans = gc.getGpa();
        cur_gpa2 = String.valueOf(ans);
    }
//    public void calcGpa2(){
//        Double ans = 0.0;
//        ans += Gpa2(sub1_take.getText());
//        ans += Gpa2(sub2_take.getText());
//        ans += Gpa2(sub3_take.getText());
//        ans += Gpa2(sub4_take.getText());
//        ans += Gpa2(sub5_take.getText());
//        ans /= 5.0;
//        cur_gpa2 = String.valueOf(ans);
//    }
//    public Double Gpa2(String sub_take){
//
//        if(Integer.parseInt(sub_take) >= 90){
//            return 4.0;
//        }
//        else if(Integer.parseInt(sub_take) >= 85 && Integer.parseInt(sub_take) < 90 ){
//            return 3.7;
//        }
//        else if(Integer.parseInt(sub_take) >= 80 && Integer.parseInt(sub_take) < 85){
//            return 3.3;
//        }
//        else if(Integer.parseInt(sub_take) >= 75 && Integer.parseInt(sub_take) < 80){
//            return 3.0;
//        }
//        else if(Integer.parseInt(sub_take) >= 70 && Integer.parseInt(sub_take) < 75){
//            return 2.7;
//        }
//        else if(Integer.parseInt(sub_take) >= 65 && Integer.parseInt(sub_take) < 70 ){
//            return 2.4;
//        }
//        else if(Integer.parseInt(sub_take) >= 60 && Integer.parseInt(sub_take) < 65){
//            return 2.2;
//        }
//        else if(Integer.parseInt(sub_take) >= 50 && Integer.parseInt(sub_take) < 60){
//            return 2.0;
//        }
//        else {
//            return 0.0;
//        }
//    }

    //5-show to user  subjects that can be assigned in the next term
    public void nextSubject(){
        alertMessage alert = new alertMessage();
        gpa_1.setText(cur_gpa1);
        gpa_2.setText(cur_gpa2);
        level_after = Integer.parseInt(cur_lvl);
        semster_after = Integer.parseInt(cur_semster) + 1;
        if(semster_after == 3){
            semster_after = 1;
            level_after++;
        }
        String query = "SELECT sub1, sub2, sub3, sub4, sub5 FROM subs WHERE " + "lvl = ? and semster = ?";
        connect = connectDB();
        if(level_after <= 4){
            try{
                pst = connect.prepareStatement(query);
                pst.setInt(1,level_after);
                pst.setInt(2,semster_after);
                rs = pst.executeQuery();
                if(rs.next()){
                    sub_1.setText(rs.getString(1));
                    sub_2.setText(rs.getString(2));
                    sub_3.setText(rs.getString(3));
                    sub_4.setText(rs.getString(4));
                    sub_5.setText(rs.getString(5));
                    PreRequest();
                    takeInformationsToStudentsNext();
                }
                else{
                    alert.errorMessage("Error Happen While Connect To Data Base");
                }
            }catch(Exception ex){ex.printStackTrace();}
        }
        else{
            //level 4 ans term 2 he is graduated if he passed his subjects

            String tmp = "";
            if(Integer.parseInt(sub1_take.getText()) < 50){
                tmp += sub1_take.getPromptText() + ", ";
            }
            if(Integer.parseInt(sub2_take.getText()) < 50){
                tmp += sub2_take.getPromptText() + ", ";
            }
            if(Integer.parseInt(sub3_take.getText()) < 50){
                tmp += sub3_take.getPromptText() + ", ";
            }
            if(Integer.parseInt(sub4_take.getText()) < 50){
                tmp += sub4_take.getPromptText() + ", ";
            }
            if(Integer.parseInt(sub5_take.getText()) < 50){
                tmp += sub5_take.getPromptText() + ", ";
            }
            if(tmp.length() > 2){
                lbl2_4grade.setText("You need to pass(" + tmp +")");
            }
            else{
                lbl2_4grade.setText("");
            }
            grade4_gpa1.setText(cur_gpa1);
            grade4_gpa2.setText(cur_gpa2);
            Grade4_pane.setVisible(true);
        }
        clearPutGpa();
    }

    //6-

    public void PreRequest(){
        int subject_1 = Integer.parseInt(sub1_take.getText());
        int subject_2 = Integer.parseInt(sub2_take.getText());
        int subject_3 = Integer.parseInt(sub3_take.getText());
        int subject_4 = Integer.parseInt(sub4_take.getText());
        int subject_5 = Integer.parseInt(sub5_take.getText());
        if(subject_1 < 50){
            lbl_1.setText(" the prerequest is " + sub1_take.getPromptText() + " you need to pass it first");
        }
        if(subject_2 < 50){
            lbl_2.setText(" the prerequest is " + sub2_take.getPromptText() + " you need to pass it first");
        }
        if(subject_3 < 50){
            lbl_3.setText(" the prerequest is " + sub3_take.getPromptText() + " you need to pass it first");
        }
        if(subject_4 < 50){
            lbl_4.setText(" the prerequest is " + sub4_take.getPromptText() + " you need to pass it first");
        }
        if(subject_5 < 50){
            lbl_5.setText(" the prerequest is " + sub5_take.getPromptText() + " you need to pass it first");
        }
    }


    //7-clear gpa fields after going to the next term subjects page
    public void clearPutGpa(){
        cur_email = "";
        cur_lvl = "";
        cur_semster = "";
        cur_username = "";
        cur_sub1 = "";
        cur_sub2 = "";
        cur_sub3 = "";
        cur_sub4 = "";
        cur_sub5 = "";
        cur_gpa1 = "A";
        cur_gpa2 = "3.7";
    }


    //8-store student new term information in database
    public void takeInformationsToStudentsNext(){

        String query = "INSERT INTO studentsnext " + "(email, lvl, semster, gpa1, gpa2, subject1, subject2, subject3, subject4, subject5, status1, status2, status3, status4, status5)"
                + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        connect = connectDB();
        try {
            alertMessage alert = new alertMessage();
            pst = connect.prepareStatement(query);
            pst.setString(1, cur_email);
            pst.setInt(2, level_after);
            pst.setInt(3, semster_after);
            pst.setString(4, cur_gpa1);
            pst.setString(5, cur_gpa2);
            pst.setString(6, sub_1.getText());
            pst.setString(7, sub_2.getText());
            pst.setString(8, sub_3.getText());
            pst.setString(9, sub_4.getText());
            pst.setString(10, sub_5.getText());
            pst.setString(11, lbl_1.getText());
            pst.setString(12, lbl_2.getText());
            pst.setString(13, lbl_3.getText());
            pst.setString(14, lbl_4.getText());
            pst.setString(15, lbl_5.getText());
            pst.executeUpdate();
            alert.successMessage("Done Successfully");

        } catch (Exception ex) { ex.printStackTrace();}
    }



    //----------for showPassword hyper link in login page----------\\
    public void showPassword(){
        if(showpass_signin.isSelected()){
            passtxt_signin.setText(pass_signin.getText());
            passtxt_signin.setVisible(true);
            pass_signin.setVisible(false);
        }
        else{
            pass_signin.setText(passtxt_signin.getText());
            passtxt_signin.setVisible(false);
            pass_signin.setVisible(true);
        }
    }

//----------Make Questions Selection----------\\
    private String[] questionArray = {"Where do you live ?", "where were you born ",
                                    "what is your father's name", "who is your best friend"};
    public void questions(){
        List<String> tmp = new ArrayList<>();
        for(int i = 0; i < 4 ; i++){
            tmp.add(questionArray[i]);
        }
        ObservableList tmp_list = FXCollections.observableArrayList(tmp);
        selectquestion_signup.setItems(tmp_list);
        selectquestion_forgetpass.setItems(tmp_list);
    }


//----------Switch Between buttons (buttons actions)----------\\
    public void switch_Between(ActionEvent e){
        if(e.getSource() == loginacc_btn || e.getSource() == back_btn_fr1 || e.getSource() == gotologin_btn){
            signup_pane.setVisible(false);
            forgetpass_pane1.setVisible(false);
            forgetpass_pane2.setVisible(false);
            signin_pane.setVisible(true);
            gpa_pane.setVisible(false);
        }
        else if(e.getSource() == createacc_btn){
            signup_pane.setVisible(true);
            signin_pane.setVisible(false);
            forgetpass_pane1.setVisible(false);
            forgetpass_pane2.setVisible(false);
        }
        else if(e.getSource() == forgetpass_signin){
            signup_pane.setVisible(false);
            signin_pane.setVisible(false);
            forgetpass_pane1.setVisible(true);
            forgetpass_pane2.setVisible(false);
        }
        else if(e.getSource() == back_btn_fr2){
            signup_pane.setVisible(false);
            signin_pane.setVisible(false);
            forgetpass_pane1.setVisible(true);
            forgetpass_pane2.setVisible(false);
        }
        else if(e.getSource() == exit_gpa_btn){
            signin_pane.setVisible(true);
            gpa_pane.setVisible(false);
            email_signin.setText("");
            pass_signin.setText("");
            passtxt_signin.setText("");
        }
        else if(e.getSource() == END_GPA || e.getSource() == END_grade4){
          END_GPA.getScene().getWindow().hide();
        }
    }
//
//public void StoreFromDB(){
////        File file = new File();
//    try (PrintWriter writer = new PrintWriter("new_file.txt")) {
//        Random random = new Random();
//        for (int i = 0; i < 100; i++) {
//            int randomNumber = random.nextInt(101);
//            writer.print(randomNumber);
//            writer.print(" ");
//        }
//    } catch (FileNotFoundException e) {
//        System.out.println(e.toString());
//    }
//    catch (Exception ex){
//        System.out.println(ex.toString());
//    }
//}
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        questions();
    }
}