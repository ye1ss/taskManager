import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Task Manager");
        frame.setBounds(400, 200, 250, 400);

        JPanel panel = new JPanel();
        JButton btns; // con una sola variable sacamos mas de uno
        btns = new JButton("Add");
        frame.add(btns);
        btns = new JButton("List");
        frame.add(btns);
        btns = new JButton("Chek");
        frame.add(btns);
        btns = new JButton("Delete");
        frame.add(btns);

        frame.setVisible(true);
    }
}
