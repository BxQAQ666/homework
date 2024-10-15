import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.*;

public class Main extends JFrame {

    public String encrypt(String s, int k) { // 加密操作
        char[] ret = new char[s.length()]; // 返回的密文
        int ret_len = 0;

        for (char x : s.toCharArray()) { // 遍历明文，若当前字符是字母则加上k
            if (x <= 'Z' && x >= 'A') {
                ret[ret_len++] = (char) ((x - 'A' + k + 26) % 26 + 'A');
            } else if (x <= 'z' && x >= 'a') {
                ret[ret_len++] = (char) ((x - 'a' + k + 26) % 26 + 'a');
            } else {
                ret[ret_len++] = x;
            }
        }

        return String.valueOf(ret);
    }

    public String decrypt(String s, int k) { // 解密操作
        char[] ret = new char[s.length()]; // 返回的明文
        int ret_len = 0;

        for (char x : s.toCharArray()) { // 遍历密文，若当前字符是字母则减去k
            if (x <= 'Z' && x >= 'A') {
                ret[ret_len++] = (char) ((x - 'A' - k + 26) % 26 + 'A');
            } else if (x <= 'z' && x >= 'a') {
                ret[ret_len++] = (char) ((x - 'a' - k + 26) % 26 + 'a');
            } else {
                ret[ret_len++] = x;
            }
        }

        return String.valueOf(ret);
    }

    public String count(String s) { // 返回一个长度为26的数组，分别记录a到z的出现次数
        int[] cnt = new int[26];

        for (char x : s.toCharArray()) {
            if (x <= 'Z' && x >= 'A') {
                cnt[x - 'A']++;
            } else if (x <= 'z' && x >= 'a') {
                cnt[x - 'a']++;
            }
        }

        String ret = "";
        for(int i = 0 ; i < 26 ; ++i)
        {
            ret += (char)('a' + i) + ":" +  cnt[i] + "  ";
        }
         
        return ret;
    }

    public String[] brute_force(String s) { // 穷举法，返回25个字符串，不包括密文本身
        String[] ret = new String[25];

        for (int i = 1; i < 26; ++i) {
            ret[i - 1] = "(offset : " + i + " )" + decrypt(s, i);//偏移量为i时的可能的明文
        }

        return ret;
    }

    public boolean is_all_num(String s) { // 判断是否全为数字，用于偏移量k的输入是否合法
        for (char x : s.toCharArray()) {
            if (x < '0' || x > '9') {
                return false;
            }
        }
        return true;
    }

    public void createJFrame() { // 定义一个CreateJFrame()方法，第一个界面
        JFrame frame = new JFrame("凯撒加解密demo");
        frame.setLayout(new FlowLayout());

        JButton bt1 = new JButton("输入加密");// 加密按钮
        bt1.addActionListener(e -> {
            createEncryptionFrame();
        });

        JButton bt2 = new JButton("输入解密");// 解密按钮
        bt2.addActionListener(e -> {
            createDecryptionFrame();
        });

        JButton bt3 = new JButton("选择文件进行处理");//对文件进行加解密操作
        bt3.addActionListener(e -> {
            JOptionPane.showMessageDialog(null , "建议选择文本文件，否则可能出现乱码");
            JFileChooser chooser = new JFileChooser(".");
            chooser.showOpenDialog(frame);
            String filePath = chooser.getSelectedFile().getAbsolutePath();
            chooseFileFrame(filePath);//跳转到文件操作界面，并传输文件的路径
        });

        frame.add(bt1);
        frame.add(bt2);
        frame.add(bt3);
        frame.setVisible(true);
        frame.setBounds(400, 300, 400, 300);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public void createEncryptionFrame() {
        JFrame Encryption_Frame = new JFrame("加密");
        Encryption_Frame.setLayout(new FlowLayout());
        Encryption_Frame.setBounds(400, 300, 400, 300);

        JLabel offsetLabel = new JLabel("偏移量：");
        Encryption_Frame.add(offsetLabel);

        JTextField offsetTextField = new JTextField(2);
        Encryption_Frame.add(offsetTextField);

        // 用户名标签
        JLabel userNameLabel = new JLabel("明文：");
        Encryption_Frame.add(userNameLabel);

        // 用户名文本框
        JTextArea userTextArea = new JTextArea();
        userTextArea.setLineWrap(true);
        Encryption_Frame.add(userTextArea);

        // 按钮确认输入完毕
        JButton confirmButton = new JButton("确定");
        confirmButton.addActionListener(e1 -> {
            if (is_all_num(offsetTextField.getText())) {// 判断偏移量是否为数字，不是则弹出提示框
                String plaintext = userTextArea.getText(); // 获取输入的明文
                int k = Integer.parseInt(offsetTextField.getText()); // 获取偏移量k

                JFrame showResultFrame = new JFrame("密文");
                showResultFrame.setLayout(new FlowLayout());
                showResultFrame.setBounds(400, 300, 400, 300);

                JLabel plainLabel = new JLabel("明文：");
                showResultFrame.add(plainLabel);

                JTextPane plaintextPane = new JTextPane();
                plaintextPane.setEditable(false);
                plaintextPane.setText(plaintext); // 显示明文
                showResultFrame.add(plaintextPane);

                JLabel cipherLabel = new JLabel("密文：");
                showResultFrame.add(cipherLabel);

                // 密文标签, 显示密文
                JTextPane ciphertextPane = new JTextPane();
                ciphertextPane.setEditable(false);
                ciphertextPane.setText(encrypt(plaintext, k)); // 显示密文
                showResultFrame.add(ciphertextPane);

                JButton bt_cnt = new JButton("显示各字母出现频率（不区分大小写）");
                bt_cnt.addActionListener(e -> {
                    JOptionPane.showMessageDialog(null, "明文：" + count(plaintext) + "\n密文：" + count(encrypt(plaintext, k)));
                });
                showResultFrame.add(bt_cnt);

                showResultFrame.setVisible(true);
                showResultFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            } else {
                JOptionPane.showMessageDialog(null, "偏移量为一个数字，请重新输入。");
            }
        });

        Encryption_Frame.add(confirmButton);
        Encryption_Frame.setVisible(true);
        Encryption_Frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    public void createDecryptionFrame() {
        JFrame Decryption_Frame = new JFrame("解密");
        Decryption_Frame.setLayout(new FlowLayout());
        Decryption_Frame.setBounds(400, 300, 400, 300);

        JLabel offsetLabel = new JLabel("偏移量：");
        Decryption_Frame.add(offsetLabel);

        JTextField offsetTextField = new JTextField(2);
        Decryption_Frame.add(offsetTextField);

        // 用户名标签
        JLabel userNameLabel = new JLabel("密文：");
        Decryption_Frame.add(userNameLabel);

        // 用户名文本框
        JTextArea userTextArea = new JTextArea();
        userTextArea.setLineWrap(true);
        Decryption_Frame.add(userTextArea);

        // 按钮确认输入完毕
        JButton confirmButton = new JButton("确定");
        confirmButton.addActionListener(e1 -> {
            if (is_all_num(offsetTextField.getText())) {// 判断偏移量是否为数字，不是则弹出提示框
                String ciphertext = userTextArea.getText(); // 获取输入的密文
                int k = Integer.parseInt(offsetTextField.getText()); // 获取偏移量k

                JFrame showResultFrame = new JFrame("明文");
                showResultFrame.setLayout(new FlowLayout());
                showResultFrame.setBounds(400, 300, 400, 300);

                JLabel plainLabel = new JLabel("密文：");
                showResultFrame.add(plainLabel);

                JTextPane plaintextPane = new JTextPane();
                plaintextPane.setEditable(false);
                plaintextPane.setText(ciphertext); // 显示密文
                showResultFrame.add(plaintextPane);

                JLabel cipherLabel = new JLabel("明文：");
                showResultFrame.add(cipherLabel);

                // 密文标签, 显示密文
                JTextPane ciphertextPane = new JTextPane();
                ciphertextPane.setEditable(false);
                ciphertextPane.setText(decrypt(ciphertext, k)); // 显示明文
                showResultFrame.add(ciphertextPane);

                JButton bt_cnt = new JButton("显示各字母出现频率（不区分大小写）");
                bt_cnt.addActionListener(e -> {
                    JOptionPane.showMessageDialog(null, "明文：" + count(decrypt(ciphertext, k)) + "\n密文：" + count(ciphertext));
                });
                showResultFrame.add(bt_cnt);

                showResultFrame.setVisible(true);
                showResultFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            } else {
                JOptionPane.showMessageDialog(null, "偏移量为一个数字，请重新输入。");
            }
        });

        Decryption_Frame.add(confirmButton);
        Decryption_Frame.setVisible(true);
        Decryption_Frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    public void chooseFileFrame(String fileName) {
        JFrame chooseFileFrame = new JFrame("文件加解密");
        chooseFileFrame.setLayout(new FlowLayout());
        chooseFileFrame.setBounds(400, 300, 400, 300);
        
        //读取文件中的内容，方便下一步操作
        StringBuilder content = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line).append(System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String Text = content.toString();

        JButton encryptButton = new JButton("加密");
        encryptButton.addActionListener(e -> {
            String userInput = JOptionPane.showInputDialog("偏移量：");
            while(!is_all_num(userInput)) {
                userInput = JOptionPane.showInputDialog("");
            }
            int k = Integer.parseInt(userInput);
            fileEncryptFrame(Text, k);
        });

        JButton decryptButton = new JButton("解密");
        decryptButton.addActionListener(e -> {
            String userInput = JOptionPane.showInputDialog("偏移量：");
            while(!is_all_num(userInput)) {
                userInput = JOptionPane.showInputDialog("偏移量为一个数字，请重新输入。");
            }
            int k = Integer.parseInt(userInput);
            fileDecryptFrame(Text, k);
        });

        JButton bruteForceButton = new JButton("穷举");//点击按钮后输出穷举结果
        bruteForceButton.addActionListener(e -> {
            String[] bruteForceResult = brute_force(Text);
            JFrame showResultFrame = new JFrame("穷举结果");
            showResultFrame.setLayout(new FlowLayout());
            showResultFrame.setBounds(400, 300, 400, 300);

            JLabel cipherLabel = new JLabel("密文：");
            showResultFrame.add(cipherLabel);

            // 密文标签, 显示密文
            JTextPane ciphertextPane = new JTextPane();
            ciphertextPane.setEditable(false);
            ciphertextPane.setText(Text); 
            showResultFrame.add(ciphertextPane);

            JLabel plainLabel = new JLabel("明文：");
            showResultFrame.add(plainLabel);

            JTextPane plaintextPane = new JTextPane();
            plaintextPane.setEditable(false);
            plaintextPane.setText(String.join(System.lineSeparator(), bruteForceResult)); // 显示穷举结果
            showResultFrame.add(plaintextPane);

            showResultFrame.setVisible(true);
            showResultFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        });/////

        chooseFileFrame.add(encryptButton);
        chooseFileFrame.add(decryptButton);
        chooseFileFrame.add(bruteForceButton);

        chooseFileFrame.setVisible(true);
        chooseFileFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    public void fileEncryptFrame(String text , int k) {
        JFrame showResultFrame = new JFrame("密文");
        showResultFrame.setLayout(new FlowLayout());
        showResultFrame.setBounds(400, 300, 400, 300);

        JLabel plainLabel = new JLabel("明文：");
        showResultFrame.add(plainLabel);

        JTextPane plaintextPane = new JTextPane();
        plaintextPane.setEditable(false);
        plaintextPane.setText(text); // 显示明文
        showResultFrame.add(plaintextPane);

        JLabel cipherLabel = new JLabel("密文：");
        showResultFrame.add(cipherLabel);

        // 密文标签, 显示密文
        JTextPane ciphertextPane = new JTextPane();
        ciphertextPane.setEditable(false);
        ciphertextPane.setText(encrypt(text, k)); // 显示密文
        showResultFrame.add(ciphertextPane);

        JButton bt_cnt = new JButton("显示各字母出现频率（不区分大小写）");
        bt_cnt.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "明文：" + count(text) + "\n密文：" + count(encrypt(text, k)));
        });
        showResultFrame.add(bt_cnt);

        showResultFrame.setVisible(true);
        showResultFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    }

    public void fileDecryptFrame(String text, int k) {
        JFrame showResultFrame = new JFrame("明文");
        showResultFrame.setLayout(new FlowLayout());
        showResultFrame.setBounds(400, 300, 400, 300);

        JLabel cipherLabel = new JLabel("密文：");
        showResultFrame.add(cipherLabel);

        // 密文标签, 显示密文
        JTextPane ciphertextPane = new JTextPane();
        ciphertextPane.setEditable(false);
        ciphertextPane.setText(text); // 显示密文
        showResultFrame.add(ciphertextPane);

        JLabel plainLabel = new JLabel("明文：");
        showResultFrame.add(plainLabel);

        JTextPane plaintextPane = new JTextPane();
        plaintextPane.setEditable(false);
        plaintextPane.setText(decrypt(text, k)); // 显示明文
        showResultFrame.add(plaintextPane);

        JButton bt_cnt = new JButton("显示各字母出现频率（不区分大小写）");
        bt_cnt.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "明文：" + count(decrypt(text, k)) + "\n密文：" + count(text));
        });
        showResultFrame.add(bt_cnt);

        showResultFrame.setVisible(true);
        showResultFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    }

    public static void main(String[] args) {
        Main m = new Main();
        m.createJFrame();
    }
}