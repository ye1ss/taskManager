import javax.swing.*;
import java.util.*;
import java.util.stream.Collectors;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.io.IOException;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

public class TaskManager {

    // ---- Modelo ----
    public static class Task {
        String title;
        boolean done;

        Task() {
        } // necesario para Gson (constructor por defecto)

        Task(String title) {
            this.title = title;
        }

        @Override
        public String toString() {
            return (done ? "[✓] " : "[ ] ") + title;
        }
    }

    // ---- Estado + JSON ----
    private final List<Task> tasks = new ArrayList<>();
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final Path store = Paths.get("tasks.json"); // o Paths.get(System.getProperty("user.home"), "tasks.json")

    // ---- UI ----
    public TaskManager() {
        load(); // carga si existe

        JFrame frame = new JFrame("Task Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(400, 200, 320, 180);

        JPanel panel = new JPanel();
        frame.add(panel);

        JButton btnAdd = new JButton("Add");
        btnAdd.addActionListener(e -> {
            addTask();
            save();
        });

        JButton btnList = new JButton("List");
        btnList.addActionListener(e -> listTasks());

        JButton btnCheck = new JButton("Check");
        btnCheck.addActionListener(e -> {
            toggleTask();
            save();
        });

        JButton btnDelete = new JButton("Delete");
        btnDelete.addActionListener(e -> {
            deleteTask();
            save();
        });

        panel.add(btnAdd);
        panel.add(btnList);
        panel.add(btnCheck);
        panel.add(btnDelete);

        frame.setVisible(true);
    }

    // ---- Acciones ----
    private void addTask() {
        String t = JOptionPane.showInputDialog("Nueva tarea:");
        if (t != null) {
            t = t.trim();
            if (!t.isEmpty())
                tasks.add(new Task(t));
        }
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
            elegida.done = !elegida.done;
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

    // ---- Persistencia JSON ----
    private void save() {
        try {
            String json = gson.toJson(tasks);
            Files.write(store, json.getBytes(StandardCharsets.UTF_8),
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "No se pudo guardar: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void load() {
        if (!Files.exists(store))
            return;
        try {
            String json = new String(Files.readAllBytes(store), StandardCharsets.UTF_8);
            Type listType = new TypeToken<List<Task>>() {
            }.getType(); // por type erasure
            List<Task> loaded = gson.fromJson(json, listType);
            if (loaded != null) {
                tasks.clear();
                tasks.addAll(loaded);
            }
        } catch (IOException | JsonSyntaxException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "No se pudo cargar el JSON (se empezará vacío): " + ex.getMessage(),
                    "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TaskManager::new);
    }
}
