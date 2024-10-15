import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.*;

public class Main extends JFrame {

    public String encrypt(String s, int k) { // 加密操作
        char[] ret = new char[s.length()]; // 返回的密文
        int ret_len = 0;

        for (char x : s.toCharArray()) { // 遍历明文，若当前字符是字母则加上k
            if (x >= 'A' && x <= 'Z') {
                ret[ret_len++] = (char) ((x - 'A' + k + 26) % 26 + 'A');
            } else if (x >= 'a' && x <= 'z') {
                ret[ret_len++] = (char) ((x - 'a' + k + 26) % 26 + 'a');
            } else {
                ret[ret_len++] = x; // 非字母字符不变
            }
        }
        return String.valueOf(ret);
    }

    public String decrypt(String s, int k) { // 解密操作
        char[] ret = new char[s.length()]; // 返回的明文
        int ret_len = 0;

        for (char x : s.toCharArray()) { // 遍历密文，若当前字符是字母则减去k
            if (x >= 'A' && x <= 'Z') {
                ret[ret_len++] = (char) ((x - 'A' - k + 26) % 26 + 'A');
            } else if (x >= 'a' && x <= 'z') {
                ret[ret_len++] = (char) ((x - 'a' - k + 26) % 26 + 'a');
            } else {
                ret[ret_len++] = x; // 非字母字符不变
            }
        }
        return String.valueOf(ret);
    }

    public String count(String s) { // 计算字母出现频率
        int[] cnt = new int[26];
        for (char x : s.toCharArray()) {
            if (x >= 'A' && x <= 'Z') {
                cnt[x - 'A']++;
            } else if (x >= 'a' && x <= 'z') {
                cnt[x - 'a']++;
            }
        }
        StringBuilder ret = new StringBuilder();
        for (int i = 0; i < 26; ++i) {
            ret.append((char) ('a' + i)).append(": ").append(cnt[i]).append("  ");
        }
        return ret.toString();
    }

    public String[] brute_force(String s) { // 穷举法
        String[] ret = new String[25];
        for (int i = 1; i < 26; ++i) {
            ret[i - 1] = "(offset: " + i + " ) " + decrypt(s, i); // 偏移量为i时的可能的明文
        }
        return ret;
    }

    public boolean is_all_num(String s) { // 判断是否全为数字
        for (char x : s.toCharArray()) {
            if (x < '0' || x > '9') {
                return false;
            }
        }
        return true;
    }

    public void createJFrame() { // 创建主界面
        JFrame frame = new JFrame("凯撒加解密demo");
        frame.setLayout(new FlowLayout());

        JButton bt1 = new JButton("输入加密");
        bt1.addActionListener(e -> createEncryptionFrame());

        JButton bt2 = new JButton("输入解密");
        bt2.addActionListener(e -> createDecryptionFrame());

        JButton bt3 = new JButton("选择文件进行处理");
        bt3.addActionListener(e -> {
            JOptionPane.showMessageDialog(null,"建议选择文本文件，否则可能出现乱码");
            JFileChooser chooser = new JFileChooser(".");
            chooser.showOpenDialog(frame);
            String filePath = chooser.getSelectedFile().getAbsolutePath();
            chooseFileFrame(filePath);
        });

        frame.add(bt1);
        frame.add(bt2);
        frame.add(bt3);
        frame.setVisible(true);
        frame.setBounds(400, 300, 400, 300);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public void createEncryptionFrame() { // 创建加密界面
        JFrame Encryption_Frame = new JFrame("加密");
        Encryption_Frame.setLayout(new FlowLayout());
        Encryption_Frame.setBounds(400, 300, 400, 300);

        JLabel offsetLabel = new JLabel("偏移量：");
        Encryption_Frame.add(offsetLabel);

        JTextField offsetTextField = new JTextField(2);
        Encryption_Frame.add(offsetTextField);

        JLabel userNameLabel = new JLabel("明文：");
        Encryption_Frame.add(userNameLabel);

        JTextArea userTextArea = new JTextArea();
        userTextArea.setLineWrap(true);
        Encryption_Frame.add(userTextArea);

        JButton confirmButton = new JButton("确定");
        confirmButton.addActionListener(e1 -> {
            if (is_all_num(offsetTextField.getText())) {
                String plaintext = userTextArea.getText();
                int k = Integer.parseInt(offsetTextField.getText());

                JFrame showResultFrame = new JFrame("密文");
                showResultFrame.setLayout(new FlowLayout());
                showResultFrame.setBounds(400, 300, 400, 300);

                JLabel plainLabel = new JLabel("明文：");
                showResultFrame.add(plainLabel);

                JTextPane plaintextPane = new JTextPane();
                plaintextPane.setEditable(false);
                plaintextPane.setText(plaintext);
                showResultFrame.add(plaintextPane);

                JLabel cipherLabel = new JLabel("密文：");
                showResultFrame.add(cipherLabel);

                JTextPane ciphertextPane = new JTextPane();
                ciphertextPane.setEditable(false);
                ciphertextPane.setText(encrypt(plaintext, k));
                showResultFrame.add(ciphertextPane);

                JButton bt_cnt = new JButton("显示各字母出现频率（不区分大小写）");
                bt_cnt.addActionListener(e -> 
                    JOptionPane.showMessageDialog(null, "明文：" + count(plaintext) + 
                                                  "\n密文：" + count(encrypt(plaintext, k)))
                );
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

    public void createDecryptionFrame() { // 创建解密界面
        JFrame Decryption_Frame = new JFrame("解密");
        Decryption_Frame.setLayout(new FlowLayout());
        Decryption_Frame.setBounds(400, 300, 400, 300);

        JLabel offsetLabel = new JLabel("偏移量：");
        Decryption_Frame.add(offsetLabel);

        JTextField offsetTextField = new JTextField(2);
        Decryption_Frame.add(offsetTextField);

        JLabel userNameLabel = new JLabel("密文：");
        Decryption_Frame.add(userNameLabel);

        JTextArea userTextArea = new JTextArea();
        userTextArea.setLineWrap(true);
        Decryption_Frame.add(userTextArea);

        JButton confirmButton = new JButton("确定");
        confirmButton.addActionListener(e1 -> {
            if (is_all_num(offsetTextField.getText())) {
                String ciphertext = userTextArea.getText();
                int k = Integer.parseInt(offsetTextField.getText());

                JFrame showResultFrame = new JFrame("明文");
                showResultFrame.setLayout(new FlowLayout());
                showResultFrame.setBounds(400, 300, 400, 300);

                JLabel cipherLabel = new JLabel("密文：");
                showResultFrame.add(cipherLabel);

                JTextPane ciphertextPane = new JTextPane();
                ciphertextPane.setEditable(false);
                ciphertextPane.setText(ciphertext);
                showResultFrame.add(ciphertextPane);

                JLabel plainLabel = new JLabel("明文：");
                showResultFrame.add(plainLabel);

                JTextPane plaintextPane = new JTextPane();
                plaintextPane.setEditable(false);
                plaintextPane.setText(decrypt(ciphertext, k));
                showResultFrame.add(plaintextPane);

                JButton bt_cnt = new JButton("显示各字母出现频率（不区分大小写）");
                bt_cnt.addActionListener(e -> 
                    JOptionPane.showMessageDialog(null, "明文：" + count(decrypt(ciphertext, k)) + 
                                                  "\n密文：" + count(ciphertext))
                );
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

    public void chooseFileFrame(String fileName) { // 创建文件处理界面
        JFrame chooseFileFrame = new JFrame("文件加解密");
        chooseFileFrame.setLayout(new FlowLayout());
        chooseFileFrame.setBounds(400, 300, 400, 300);

        StringBuilder content = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line).append(System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String text = content.toString();

        JButton encryptButton = new JButton("加密");
        encryptButton.addActionListener(e -> {
            String userInput = JOptionPane.showInputDialog("偏移量：");
            while (!is_all_num(userInput)) {
                userInput = JOptionPane.showInputDialog("偏移量为一个数字，请重新输入。");
            }
            int k = Integer.parseInt(userInput);
            fileEncryptFrame(text, k);
        });

        JButton decryptButton = new JButton("解密");
        decryptButton.addActionListener(e -> {
            String userInput = JOptionPane.showInputDialog("偏移量：");
            while (!is_all_num(userInput)) {
                userInput = JOptionPane.showInputDialog("偏移量为一个数字，请重新输入。");
            }
            int k = Integer.parseInt(userInput);
            fileDecryptFrame(text, k);
        });

        JButton bruteForceButton = new JButton("穷举");
        bruteForceButton.addActionListener(e -> {
            String[] bruteForceResult = brute_force(text);
            JFrame showResultFrame = new JFrame("穷举结果");
            showResultFrame.setLayout(new FlowLayout());
            showResultFrame.setBounds(400, 300, 400, 300);

            JLabel cipherLabel = new JLabel("密文：");
            showResultFrame.add(cipherLabel);

            JTextPane ciphertextPane = new JTextPane();
            ciphertextPane.setEditable(false);
            ciphertextPane.setText(text);
            showResultFrame.add(ciphertextPane);

            JLabel plainLabel = new JLabel("明文：");
            showResultFrame.add(plainLabel);

            JTextPane plaintextPane = new JTextPane();
            plaintextPane.setEditable(false);
            plaintextPane.setText(String.join(System.lineSeparator(), bruteForceResult));
            showResultFrame.add(plaintextPane);

            showResultFrame.setVisible(true);
            showResultFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        });

        chooseFileFrame.add(encryptButton);
        chooseFileFrame.add(decryptButton);
        chooseFileFrame.add(bruteForceButton);
        chooseFileFrame.setVisible(true);
        chooseFileFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    public void fileEncryptFrame(String text, int k) { // 显示文件加密结果
        JFrame showResultFrame = new JFrame("密文");
        showResultFrame.setLayout(new FlowLayout());
        showResultFrame.setBounds(400, 300, 400, 300);

        JLabel plainLabel = new JLabel("明文：");
        showResultFrame.add(plainLabel);

        JTextPane plaintextPane = new JTextPane();
        plaintextPane.setEditable(false);
        plaintextPane.setText(text);
        showResultFrame.add(plaintextPane);

        JLabel cipherLabel = new JLabel("密文：");
        showResultFrame.add(cipherLabel);

        JTextPane ciphertextPane = new JTextPane();
        ciphertextPane.setEditable(false);
        ciphertextPane.setText(encrypt(text, k));
        showResultFrame.add(ciphertextPane);

        JButton bt_cnt = new JButton("显示各字母出现频率（不区分大小写）");
        bt_cnt.addActionListener(e ->
            JOptionPane.showMessageDialog(null, "明文：" + count(text) + 
                                          "\n密文：" + count(encrypt(text, k)))
        );
        showResultFrame.add(bt_cnt);
        showResultFrame.setVisible(true);
        showResultFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    public void fileDecryptFrame(String text, int k) { // 显示文件解密结果
        JFrame showResultFrame = new JFrame("明文");
        showResultFrame.setLayout(new FlowLayout());
        showResultFrame.setBounds(400, 300, 400, 300);

        JLabel cipherLabel = new JLabel("密文：");
        showResultFrame.add(cipherLabel);

        JTextPane ciphertextPane = new JTextPane();
        ciphertextPane.setEditable(false);
        ciphertextPane.setText(text);
        showResultFrame.add(ciphertextPane);

        JLabel plainLabel = new JLabel("明文：");
        showResultFrame.add(plainLabel);

        JTextPane plaintextPane = new JTextPane();
        plaintextPane.setEditable(false);
        plaintextPane.setText(decrypt(text, k));
        showResultFrame.add(plaintextPane);

        JButton bt_cnt = new JButton("显示各字母出现频率（不区分大小写）");
        bt_cnt.addActionListener(e ->
            JOptionPane.showMessageDialog(null, "明文：" + count(decrypt(text, k)) + 
                "\n密文：" + count(text))
        );
        showResultFrame.add(bt_cnt);
        showResultFrame.setVisible(true);
        showResultFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    public static void main(String[] args) {
        Main m = new Main();
        m.createJFrame();
    }
}
