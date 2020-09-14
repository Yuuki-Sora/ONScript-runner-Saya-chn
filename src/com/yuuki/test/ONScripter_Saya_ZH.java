package com.yuuki.test;

import org.newdawn.easyogg.OggClip;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Scanner;

public class ONScripter_Saya_ZH extends JFrame {

    private final int screen_width = 800;
    private final int screen_height = 600;
    private final int offset_x = 10;
    private final int offset_y = 117;

    public ONScripter_Saya_ZH() throws Exception {
        JFrame frame = new JFrame();
        frame.setSize(screen_width + 14, screen_height + 37);
        frame.setTitle("ONScripter that only works with the original port of SayaNoUta_ZH");
        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
        MyJPanel myJPanel = new MyJPanel();
        myJPanel.repaint();
        frame.getContentPane().add(myJPanel);
        myJPanel.setBounds(0, 0, screen_width, screen_height);
        frame.setVisible(true);
        frame.setResizable(false);
    }

    public static void main(String[] args) throws Exception {
        new ONScripter_Saya_ZH();
    }

    public class MyJPanel extends JPanel implements ActionListener, MouseListener, MouseMotionListener, KeyListener {

        Image cg;
        Image ConvBG;
        Image title_screen;
        Image char_pic1;
        Image char_pic2;
        Image char_pic3;
        Timer timer;
        String read;
        String sub = "0";
        String t = "";
        String name = "";
        String word = "";
        String bg = "res/bg.png";
        String CG = "";
        String BGM = "";
        String[] char_pic = new String[3];
        String[] subWord = new String[4];
        String[] stc = new String[2];
        String[] findStc = new String[2];
        int clicked = 1;
        int Name_Exist = 0;
        int Useless_Line = 1;
        int start_game = 0;
        int title = 0;
        int[] image_x = new int[3];
        int[] image_y = new int[3];
        int gameProgress = 0;
        int loadGameProgress = 0;
        int ctrled = 0;
        int bgmStop = 0;
        int bgmChanged = 0;
        OggClip bgm = new OggClip("");
        OggClip voice = new OggClip("");
        OggClip titleBgm = new OggClip("");

        File file = new File("res/0.txt");
        File save = new File("res/save.s");
        Scanner sc = new Scanner(file);

        void saveGame() throws IOException {
            Scanner readG = new Scanner(save);
            Writer saver = new FileWriter(save, false);
            saver.write("" + gameProgress);
            saver.close();
            JOptionPane.showMessageDialog(null, "Game saved successfully to \"save.s\". Don't remove this file. ");
        }

        void readGame() throws FileNotFoundException {

            //Stop title BGM
            titleBgm.stop();

            //Stop current BGM
            if(!bgm.stopped())
                bgm.stop();

            //Stop character voice
            if(!voice.stopped())
                voice.stop();

            //Read saved file
            Scanner readG = new Scanner(save);

            //Update in-game parameters
            if(readG.hasNextLine()){
                loadGameProgress = Integer.parseInt(readG.nextLine());
                JOptionPane.showMessageDialog(null, "Game loaded successfully from \"save.s\". Press \"Okay\" to continue playing. ");
                File file = new File("res/0.txt");
                Scanner sc1 = new Scanner(file);
                for(int i = 0; i <= loadGameProgress; i++){
                    read = sc1.nextLine();

                    //Display CG file after reading
                    if(read.indexOf("cg\\") == 4){
                        sub = read.substring(read.indexOf("\"") + 1, read.lastIndexOf("\""));
                        char_pic[0] = "0";
                        ImageIcon icon4 = new ImageIcon(char_pic[0]);
                        char_pic1 = icon4.getImage();
                        char_pic[1] = "0";
                        ImageIcon icon5 = new ImageIcon(char_pic[1]);
                        char_pic2 = icon5.getImage();
                        char_pic[2] = "0";
                        ImageIcon icon6 = new ImageIcon(char_pic[2]);
                        char_pic3 = icon6.getImage();
                    }
                    CG = "res\\" + sub;
                    ImageIcon icon3 = new ImageIcon(CG);
                    cg = icon3.getImage();

                    //Display char file after reading
                    if(read.contains("ld1 l,")){
                        char_pic[0] = "res\\" + read.substring(read.indexOf("\"") + 1, read.lastIndexOf("\""));
                        ImageIcon icon4 = new ImageIcon(char_pic[0]);
                        char_pic1 = icon4.getImage();
                        image_x[0] = char_pic1.getWidth(this);
                        image_y[0] = char_pic1.getHeight(this);
                    }
                    if(read.contains("ld1 c,")){
                        char_pic[1] = "res\\" + read.substring(read.indexOf("\"") + 1, read.lastIndexOf("\""));
                        ImageIcon icon5 = new ImageIcon(char_pic[1]);
                        char_pic2 = icon5.getImage();
                        image_x[1] = char_pic2.getWidth(this);
                        image_y[1] = char_pic2.getHeight(this);
                    }
                    if(read.contains("ld1 r,")){
                        char_pic[2] = "res\\" + read.substring(read.indexOf("\"") + 1, read.lastIndexOf("\""));
                        ImageIcon icon6 = new ImageIcon(char_pic[2]);
                        char_pic3 = icon6.getImage();
                        image_x[2] = char_pic3.getWidth(this);
                        image_y[2] = char_pic3.getHeight(this);
                    }
                    if(read.contains("cspd l")){
                        char_pic[0] = "0";
                        ImageIcon icon4 = new ImageIcon(char_pic[0]);
                        char_pic1 = icon4.getImage();
                    }
                    if(read.contains("cspd c")){
                        char_pic[1] = "0";
                        ImageIcon icon5 = new ImageIcon(char_pic[1]);
                        char_pic2 = icon5.getImage();
                    }
                    if(read.contains("cspd r")){
                        char_pic[2] = "0";
                        ImageIcon icon6 = new ImageIcon(char_pic[2]);
                        char_pic3 = icon6.getImage();
                    }

                    //Play bgm after reading
                    if (read.contains("bgm")){
                        if(read.contains("bgm ")) {
                            BGM = read.substring(read.indexOf("\"") + 1, read.lastIndexOf("\"")).toLowerCase();
                        }
                    }
                }
                bgmChanged = 1;
                sc = sc1;
                start_game = 0;
                ONScripter_Saya_ZH.this.repaint();
                gameProgress = loadGameProgress;
            }
        }

        MyJPanel() throws Exception{
            JButton saveButton = new JButton("SAVE");
            JButton loadButton = new JButton("LOAD");
            setLayout(new FlowLayout(FlowLayout.RIGHT));
            save.createNewFile();
            saveButton.setToolTipText("Do you want to save your game or not? OwO");
            loadButton.setToolTipText("Do you want to load your game or not? OwO");
            add(saveButton);
            add(loadButton);
            saveButton.addKeyListener(this);
            loadButton.addKeyListener(this);
            saveButton.addActionListener(e -> {
                if(start_game == 1) {
                    try {
                        saveGame();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            });
            loadButton.addActionListener(e -> {
                try {
                    readGame();
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                }
            });
            setBackground(Color.black);
            read = sc.nextLine();
            gameProgress++;
            subWord[0] = "";
            subWord[1] = "";
            subWord[2] = "";
            subWord[3] = "";
            addMouseListener(this);
            addMouseMotionListener(this);
            addKeyListener(this);
            setFocusable(true);
            timer = new Timer(1, this);
            timer.start();
        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            setBackground(Color.black);
            try {
                g.drawImage(cg, screen_width / 2 - cg.getWidth(this) / 2, screen_height / 2 - cg.getHeight(this) / 2, this);
                g.drawImage(char_pic1, screen_width / 2 - cg.getWidth(this) / 2, screen_height / 2 + cg.getHeight(this) / 2 - image_y[0], this);
                g.drawImage(char_pic2, screen_width / 2 - image_x[1] / 2, screen_height / 2 + cg.getHeight(this) / 2 - image_y[1], this);
                g.drawImage(char_pic3, screen_width / 2 + cg.getWidth(this) / 2 - image_x[2], screen_height / 2 + cg.getHeight(this) / 2 - image_y[2], this);
                g.drawImage(ConvBG, screen_width / 2 - ConvBG.getWidth(this) / 2, screen_height / 2 + 88, this);
                g.setFont(new Font("DengXian", Font.PLAIN, 25));
                g.setColor(Color.white);
                g.drawString(name, screen_width / 2 - cg.getWidth(this) / 2 + offset_x, screen_height / 2 + offset_y);
                for (int i = 0; i < 4; i++) {
                    if (subWord[i].length() != 0)
                        g.drawString(subWord[i], screen_width / 2 - cg.getWidth(this) / 2 + offset_x, screen_height / 2 + offset_y + (i + 1) * 40);
                }
                if (start_game == 0) {
                    g.drawImage(title_screen, screen_width / 2 - title_screen.getWidth(this) / 2, screen_height / 2 - title_screen.getHeight(this) / 2, this);
                }
            }catch (NullPointerException ex) {
                System.out.println("Oops, failed to draw something on the screen... ");
            }
        }

        public void actionPerformed(ActionEvent e) {

            //Display title screen
            if(start_game == 0 && title == 0){
                read = sc.nextLine();
                gameProgress++;
                if(read.contains("bg \":r;")) {
                    t = "res\\" + read.substring(read.indexOf(";") + 1, read.lastIndexOf("\""));
                    System.out.println(t);
                    title = 1;
                    ImageIcon icon = new ImageIcon(t);
                    title_screen = icon.getImage();
                    cg = title_screen;
                }
                //Play title BGM
                if (read.contains("bgm")){
                    try {
                        if(read.contains("bgm ")) {
                            titleBgm = new OggClip((read.substring(read.indexOf("\"") + 1, read.lastIndexOf("\""))).toLowerCase());
                            titleBgm.loop();
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }

            ImageIcon icon2 = new ImageIcon(bg);
            ConvBG = icon2.getImage();

            Useless_Line = 0;
            setBackground(Color.black);

            if ((clicked == 1 && start_game == 1) || ctrled == 1) {

                //Stop title bgm
                titleBgm.stop();

                //Read CG file
                read = sc.nextLine();
                gameProgress++;
                if (read.indexOf("cg\\") == 4){
                    sub = read.substring(read.indexOf("\"") + 1, read.lastIndexOf("\""));
                    char_pic[0] = "0";
                    ImageIcon icon4 = new ImageIcon(char_pic[0]);
                    char_pic1 = icon4.getImage();
                    char_pic[1] = "0";
                    ImageIcon icon5 = new ImageIcon(char_pic[1]);
                    char_pic2 = icon5.getImage();
                    char_pic[2] = "0";
                    ImageIcon icon6 = new ImageIcon(char_pic[2]);
                    char_pic3 = icon6.getImage();
                }

                //Display CG file
                CG = "res\\" + sub;
                ImageIcon icon3 = new ImageIcon(CG);
                cg = icon3.getImage();

                //Display or remove character pic left to right
                if(read.contains("ld1 l,")){
                    char_pic[0] = "res\\" + read.substring(read.indexOf("\"") + 1, read.lastIndexOf("\""));
                    ImageIcon icon4 = new ImageIcon(char_pic[0]);
                    char_pic1 = icon4.getImage();
                    image_x[0] = char_pic1.getWidth(this);
                    image_y[0] = char_pic1.getHeight(this);
                }
                if(read.contains("ld1 c,")){
                    char_pic[1] = "res\\" + read.substring(read.indexOf("\"") + 1, read.lastIndexOf("\""));
                    ImageIcon icon5 = new ImageIcon(char_pic[1]);
                    char_pic2 = icon5.getImage();
                    image_x[1] = char_pic2.getWidth(this);
                    image_y[1] = char_pic2.getHeight(this);
                }
                if(read.contains("ld1 r,")){
                    char_pic[2] = "res\\" + read.substring(read.indexOf("\"") + 1, read.lastIndexOf("\""));
                    ImageIcon icon6 = new ImageIcon(char_pic[2]);
                    char_pic3 = icon6.getImage();
                    image_x[2] = char_pic3.getWidth(this);
                    image_y[2] = char_pic3.getHeight(this);
                }
                if(read.contains("cspd l")){
                    char_pic[0] = "0";
                    ImageIcon icon4 = new ImageIcon(char_pic[0]);
                    char_pic1 = icon4.getImage();
                }
                if(read.contains("cspd c")){
                    char_pic[1] = "0";
                    ImageIcon icon5 = new ImageIcon(char_pic[1]);
                    char_pic2 = icon5.getImage();
                }
                if(read.contains("cspd r")){
                    char_pic[2] = "0";
                    ImageIcon icon6 = new ImageIcon(char_pic[2]);
                    char_pic3 = icon6.getImage();
                }

                //Read word without character
                int count = 0;
                char check;
                String con_check;
                for(int i = 0; i < 26; i++) {
                    check = (char) (97 + i);
                    con_check = Character.toString(check);
                    if(!read.contains(con_check))
                        count++;
                }
                if(count == 26 && read.length() != 0) {
                    System.out.println("This is a line without characters. ");
                    Useless_Line++;
                    name = "";
                    word = read;
                    subWord[0] = word;
                    subWord[1] = "";
                    subWord[2] = "";
                    subWord[3] = "";
                    if (word.length() > 32 && read.length() <= 64) {
                        subWord[0] = word.substring(0, 31);
                        subWord[1] = word.substring(31);
                    }
                    if (read.length() > 64 && read.length() <= 96) {
                        subWord[0] = word.substring(0, 31);
                        subWord[1] = word.substring(31, 62);
                        subWord[2] = word.substring(62);
                    }
                    if (read.length() > 96) {
                        subWord[0] = word.substring(0, 31);
                        subWord[1] = word.substring(31, 62);
                        subWord[2] = word.substring(62, 93);
                        subWord[3] = word.substring(93);
                    }
                }

                //Read and display name of characters
                if (read.indexOf("name") != 0) {
                    Useless_Line++;
                } else {
                    System.out.println("This is a name & character line. ");
                    Name_Exist = 1;
                    name = read.substring(read.indexOf("\"") + 1, read.lastIndexOf("\""));
                }

                //Read and display lines of characters
                if (Name_Exist == 1) {
                    Name_Exist = 0;
                    word = sc.nextLine();
                    gameProgress++;
                    subWord[0] = word;
                    subWord[1] = "";
                    subWord[2] = "";
                    subWord[3] = "";
                    if (word.length() > 32 && read.length() <= 64) {
                        subWord[0] = word.substring(0, 31);
                        subWord[1] = word.substring(31);
                    }
                    if (read.length() > 64 && read.length() <= 96) {
                        subWord[0] = word.substring(0, 31);
                        subWord[1] = word.substring(31, 62);
                        subWord[2] = word.substring(62);
                    }
                    if (read.length() > 96) {
                        subWord[0] = word.substring(0, 31);
                        subWord[1] = word.substring(31, 62);
                        subWord[2] = word.substring(62, 93);
                        subWord[3] = word.substring(93);
                    }
                }

                //Read options and select an option
                if(read.contains("select ")){
                    stc[0] = read.substring(read.indexOf(" \"") + 2, read.indexOf("\",*"));
                    findStc[0] = read.substring(read.indexOf("\",*") + 3, read.lastIndexOf(",\""));
                    stc[1] = read.substring(read.indexOf(",\"") + 2, read.lastIndexOf("\",*"));
                    findStc[1] = read.substring(read.lastIndexOf("\",*") + 3);
                    int s = JOptionPane.showOptionDialog(null, "Options are provided by the game. Choose one to continue. ",
                            "Options available. ",
                            JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, stc, stc[0]);
                    if(s == 0){
                        int tempS = 1;
                        while (tempS != 0){
                            if (sc.nextLine().contains(findStc[0]))
                                tempS = 0;
                        }
                    }
                    if(s == 1){
                        int tempS = 1;
                        while (tempS != 0){
                            if (sc.nextLine().contains(findStc[1]))
                                tempS = 0;
                        }
                    }
                }

                //End game
                if(read.contains("END")) {
                    JOptionPane.showMessageDialog(null, "Game ended. ");
                    System.exit(0);
                }

                if(read.contains("bgm ")) {
                    BGM = read.substring(read.indexOf("\"") + 1, read.lastIndexOf("\"")).toLowerCase();
                    bgmChanged = 1;
                }
                if (read.contains("bgmstop")){
                    bgmStop = 1;
                }

                if (clicked == 1 && start_game == 1 && ctrled != 1){
                    //Play & stop BGM
                    try {
                        if (bgmStop == 1 || read.contains("bgmstop")){
                            bgm.stop();
                            bgmStop = 0;
                        }
                        if(read.contains("bgm ") || bgmChanged == 1) {
                            File temp = new File("res/" + BGM);
                            if(temp.exists()) {
                                bgm.stop();
                                bgm = new OggClip(BGM);
                                bgm.loop();
                            }
                            bgmChanged = 0;
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }

                    //Play & stop character voice
                    if(read.contains("dwave 1")){
                        if(!voice.stopped())
                            voice.stop();
                        try {
                            voice = new OggClip((read.substring(read.indexOf("\"") + 1, read.lastIndexOf("\""))).toLowerCase());
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        voice.play();
                    }
                }

                clicked = 0;
            }

            if (Useless_Line == 1) {
                clicked = 1;
            }
            super.repaint();
        }
        public void mouseClicked(MouseEvent me){
            start_game = 1;
            if(sc.hasNextLine()) {
                clicked = 1;
            }
            System.out.println("Current progress in NScript: " + gameProgress);
        }
        public void mousePressed(MouseEvent me) {}
        public void mouseReleased (MouseEvent me){}
        public void mouseExited (MouseEvent me){}
        public void mouseEntered (MouseEvent me){}
        public void mouseMoved (MouseEvent me){}
        public void mouseDragged (MouseEvent me){}
        public void keyPressed (KeyEvent e){
            int key = e.getKeyCode();
            if(key == KeyEvent.VK_CONTROL && start_game == 1){
                System.out.println("Ctrl pressed. ");
                ctrled = 1;
            }
        }
        public void keyTyped (KeyEvent e){}
        public void keyReleased (KeyEvent e){
            int key = e.getKeyCode();
            if(key == KeyEvent.VK_CONTROL){
                System.out.println("Ctrl released. ");
                ctrled = 0;
            }
        }
    }

}
