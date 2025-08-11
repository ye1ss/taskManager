import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class main {

    // Modelo de datos
    private static class Task {
        String title;
        boolean done;

        Task(String title) {
            this.title = title;
        }

        @Override
        public String toString() {
            return (done ? "[✓] " : "[ ] ") + title;
        }
    }

    private final List<Task> tasks = new ArrayList<>();

    public main() {
        JFrame frame = new JFrame("Task Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(400, 200, 300, 180);

        JPanel panel = new JPanel();
        frame.add(panel);

        JButton btnAdd = new JButton("Add");
        btnAdd.addActionListener(e -> addTask());

        JButton btnList = new JButton("List");
        btnList.addActionListener(e -> listTasks());

        JButton btnCheck = new JButton("Check");
        btnCheck.addActionListener(e -> toggleTask());

        JButton btnDelete = new JButton("Delete");
        btnDelete.addActionListener(e -> deleteTask());

        panel.add(btnAdd);
        panel.add(btnList);
        panel.add(btnCheck);
        panel.add(btnDelete);

        frame.setVisible(true);
    }

    private void addTask() {
        String t = JOptionPane.showInputDialog("Nueva tarea:");
        if (t != null && !t.isBlank())
            tasks.add(new Task(t.trim()));
    }

    private void listTasks() {
        if (tasks.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay tareas");
            return;
        }
        String body = tasks.stream().map(Task::toString).collect(Collectors.joining("\n"));
        JOptionPane.showMessageDialog(null, body, "Tareas", JOptionPane.INFORMATION_MESSAGE);
    }

    private void toggleTask() {
        if (tasks.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay tareas para marcar.");
            return;
        }
        Task[] opciones = tasks.toArray(new Task[0]);
        Task elegida = (Task) JOptionPane.showInputDialog(
                null, "Selecciona tarea (marcar/desmarcar):",
                "Check", JOptionPane.PLAIN_MESSAGE, null,
                opciones, opciones[0]);
        if (elegida != null)
            elegida.done = !elegida.done; // ← aquí SÍ se guarda el cambio
    }

    private void deleteTask() {
        if (tasks.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay tareas para borrar.");
            return;
        }
        Task[] opciones = tasks.toArray(new Task[0]);
        Task elegida = (Task) JOptionPane.showInputDialog(
                null, "Selecciona tarea a borrar:",
                "Delete", JOptionPane.PLAIN_MESSAGE, null,
                opciones, opciones[0]);
        if (elegida != null)
            tasks.remove(elegida);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(main::new);
    }
}
