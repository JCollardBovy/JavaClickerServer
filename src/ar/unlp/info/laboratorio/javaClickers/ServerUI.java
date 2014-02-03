package ar.unlp.info.laboratorio.javaClickers;

import ar.unlp.info.laboratorio.javaClickers.model.*;
import ar.unlp.info.laboratorio.javaClickers.server.Server;
import ar.unlp.info.laboratorio.javaClickers.network.ServerManager;
import ar.unlp.info.laboratorio.javaClickers.auxiliary.Par;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.RangeType;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.*;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Jony
 * Date: 14/09/13
 *
 * Time: 17:35
 * To change this template use File | Settings | File Templates.
 */
public class ServerUI {
    private JPanel mainPanel;
    private JTabbedPane tabbedPane1;
    private JButton nextButton;
    private ChartPanel resultsTable;
    private JTextArea problemAssigment;
    private JButton addProblem;
    private JButton deleteProblem;
    private JList problemList;
    private JTextArea assigmentField;
    private JPanel panelNewSolution;
    private JTextField solutionField;
    private JButton addSolution;
    private JList solutionList;
    private JComboBox chooseNextProblem;
    private JButton addNextProblem;
    private JList nextProblemList;
    private JButton moveProblemUp;
    private JButton moveProblemDown;
    private JButton confirmProblem;
    private JButton cancelProblem;
    private JComboBox correctSolution;
    private JTextField timeField;
    private JPanel problemForm;
    private JButton removeNextProblem;
    private JComboBox firstProblemCombo;
    private JButton runButton;
    private JPanel timePanel;
    private JLabel timeLabel;
    private JLabel remainingTimeLabel;
    private JPanel problemButtons;
    private JPanel problems;
    private JTextField subjectName;
    private JPanel lessonPanel;
    private JTextField topicField;
    private JButton addTopic;
    private JList topicList;
    private JButton deleteTopic;

    Server server;
    Editor editor;
    Lesson aLesson = new Lesson();;
    Run run;

    public ServerUI() {
        server = new Server();
        editor = new Editor();
        run = new Run();
        setUpTabbedPane();
    }

    private void setUpTabbedPane() {
        tabbedPane1.setSelectedIndex(0);
        tabbedPane1.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (tabbedPane1.getSelectedIndex() == 0){
                    editor.updateInformation();
                    run.loadInformation();
                }else{
                    editor.loadInformation();
                }
            }
        });
    }

    private void createUIComponents() {
        resultsTable = new ChartPanel(null);
    }

    private class Editor{

        private ProblemNode currentProblem;
        private DefaultComboBoxModel<Solution> solutions = new DefaultComboBoxModel<Solution>();
        private DefaultComboBoxModel<ProblemNode> problems = new DefaultComboBoxModel<ProblemNode>();
        private DefaultComboBoxModel<ProblemNode> nextPossibleProblems = new DefaultComboBoxModel<ProblemNode>();
        private DefaultComboBoxModel<ProblemNode> nextProblems = new DefaultComboBoxModel<ProblemNode>();
        private DefaultComboBoxModel<String> topicModel = new DefaultComboBoxModel<String>();

        private Editor() {

            problemList.setModel(problems);
            addProblem();
            deleteProblem();
            addSolution();
            setUpTimerField();
            setUpAddTopic();
            setUpDeleteTopic();
            addNextProblem();
            removeNextProblem();
            moveProblemUp();
            moveProblemDown();
            confirmProblem();
            cancelProblem();
            problemList();
            enabledForm(false);
            loadMockData();
        }

        private void loadMockData() {
            List<ProblemNode> nodeList = new LinkedList<ProblemNode>();
            for (int i=1; i<=4; i++){
                HashSet<Solution> solutions = new HashSet<Solution>();
                Problem problem = new Problem("Problema "+i, 10+i, solutions, null);
                solutions.add(new Solution("Solucion 1", problem));
                solutions.add(new Solution("Solucion 2 (Correcta)", problem));
                solutions.add(new Solution("Solucion 3", problem));
                problem.setCorrectSolution((Solution) solutions.toArray()[2]);
                nodeList.add(new ProblemNode(problem, new TreeMap<Integer, ProblemNode>()));
                /*if ((i % 3)==0){
                    listToMap(nodeList.subList(1, i%3), nodeList.get(0+(i%3)).getNextProblems());
                }*/
                if (i == 4){
                    listToMap(nodeList.subList(1, 4), nodeList.get(0).getNextProblems());
                }
                problems.addElement(nodeList.get(i-1));
            }


        }

        private DefaultComboBoxModel<ProblemNode> getProblems() {
            return problems;
        }

        private void setUpAddTopic(){
            addTopic.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    topicModel.addElement(topicField.getText());
                }
            });
        }

        private void setUpDeleteTopic(){
            deleteTopic.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (topicList.getSelectedIndex() != -1){
                        topicModel.removeElement(topicList.getSelectedValue());
                    }
                }
            });
        }

        private void setUpTimerField() {
            timeField.addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    timeField.setText(timeField.getText().replaceAll("[^0-9]", ""));
                }
            });
        }

        private void removeNextProblem() {
            removeNextProblem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (nextProblemList.getSelectedIndex() != -1){
                        nextProblems.removeElement(nextProblemList.getSelectedValue());
                    }
                }
            });
        }

        private void moveProblemUp() {
            moveProblemUp.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if ((nextProblemList.getSelectedIndex() > 0)){
                        ProblemNode aProblemNode = (ProblemNode) nextProblemList.getSelectedValue();
                        nextProblems.insertElementAt(aProblemNode, nextProblemList.getSelectedIndex() - 1);
                        nextProblems.removeElementAt(nextProblemList.getSelectedIndex());
                        nextProblemList.setSelectedValue(aProblemNode, true);
                    }
                }
            });
        }

        private void moveProblemDown(){
            moveProblemDown.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if ((nextProblemList.getSelectedIndex() != -1)&&(nextProblemList.getSelectedIndex() < nextProblems.getSize() - 1)){
                        ProblemNode aProblemNode = (ProblemNode) nextProblemList.getSelectedValue();
                        nextProblems.insertElementAt(aProblemNode, nextProblemList.getSelectedIndex() + 2);
                        nextProblems.removeElementAt(nextProblemList.getSelectedIndex());
                        nextProblemList.setSelectedValue(aProblemNode, true);
                    }
                }
            });
        }

        private void cancelProblem() {
            cancelProblem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    enabledForm(false);
                    clearForm();
                    currentProblem = null;
                }
            });
        }

        private void problemList() {
            problemList.addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent e) {
                    if (problemList.getSelectedIndex() != -1){
                        currentProblem = (ProblemNode) problemList.getSelectedValue();
                        clearForm();
                        contextChange();     //TODO cargar los campos q faltan del formulario
                        enabledForm(true);
                    }
                }
            });
        }

        private void addNextProblem() {
            addNextProblem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (chooseNextProblem.getSelectedIndex() != -1){
                        nextProblems.addElement((ProblemNode) chooseNextProblem.getSelectedItem());
                        nextPossibleProblems.removeElement(chooseNextProblem.getSelectedItem());
                    }
                }
            });
        }

        private void contextChange(){
            assigmentField.setText(currentProblem.getProblem().getAssigment());
            solutions = new DefaultComboBoxModel<Solution>(new Vector<Solution>(currentProblem.getProblem().getSolutions()));
            solutionList.setModel(solutions);
            correctSolution.setModel(solutions);
            correctSolution.setSelectedItem(currentProblem.getProblem().getCorrectSolution());
            timeField.setText(String.valueOf(currentProblem.getProblem().getTime()));
            nextProblems = new DefaultComboBoxModel<ProblemNode>(new Vector<ProblemNode>(currentProblem.getNextProblems().values()));
            nextProblemList.setModel(nextProblems);
            if (problems.getSize() > 0){
                copyAllProblems();
            }
            chooseNextProblem.setModel(nextPossibleProblems);
            chooseNextProblem.setSelectedIndex(-1);
        }

        private void copyAllProblems() {
            for (int i=0; i<problems.getSize(); i++){
                nextPossibleProblems.addElement(problems.getElementAt(i));
            }
            nextPossibleProblems.removeElement(currentProblem);
            for (int i=0; i<nextProblems.getSize(); i++){
                nextPossibleProblems.removeElement(nextProblems.getElementAt(i));
            }
        }

        private void confirmProblem() {
            confirmProblem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if ((!assigmentField.getText().isEmpty())&&(solutions.getSize() > 0)&&(correctSolution.getSelectedIndex() != -1 )&&(!timeField.getText().isEmpty())){
                        currentProblem.getProblem().setAssigment(assigmentField.getText());
                        currentProblem.getProblem().setCorrectSolution((Solution) correctSolution.getSelectedItem());
                        currentProblem.getProblem().setTime(Long.valueOf(timeField.getText()));
                        modelToCollection(solutions, currentProblem.getProblem().getSolutions());
                        saveNextProblems();
                        if (problems.getIndexOf(currentProblem) == -1){
                            problems.addElement(currentProblem);
                        }
                        currentProblem = null;
                        enabledForm(false);
                        clearForm();
                    }else{
                        JOptionPane.showMessageDialog(tabbedPane1, "Falta completar algún campo del formulario");
                    }
                }
            });
        }

        private void saveNextProblems() {
            if (nextProblems.getSize() > 0){
                List<ProblemNode> tmp = new LinkedList<ProblemNode>();
                modelToCollection(nextProblems, tmp);
                listToMap(tmp, currentProblem.getNextProblems());
            }
        }

        private void listToMap(List<ProblemNode> tmp, TreeMap<Integer, ProblemNode> nextProblems) {
            nextProblems.clear();
            int j = 100/tmp.size();
            for (int i=0; i<tmp.size(); i++){
                nextProblems.put(j*(i+1), tmp.get(i));
            }
        }

        private void enabledForm(Boolean enabled) {
            assigmentField.setEnabled(enabled);
            solutionField.setEnabled(enabled);
            addSolution.setEnabled(enabled);
            solutionList.setEnabled(enabled);
            correctSolution.setEnabled(enabled);
            timeField.setEnabled(enabled);
            chooseNextProblem.setEnabled(enabled && (problems.getSize() > 0));
            addNextProblem.setEnabled(enabled && (problems.getSize() > 0));
            removeNextProblem.setEnabled(enabled && (problems.getSize() > 0));
            nextProblemList.setEnabled(enabled);
            moveProblemUp.setEnabled(enabled);
            moveProblemDown.setEnabled(enabled);
            confirmProblem.setEnabled(enabled);
            cancelProblem.setEnabled(enabled);
            addProblem.setEnabled(!enabled);
            deleteProblem.setEnabled(!enabled);
            problemList.setEnabled(!enabled);

        }

        private void modelToCollection(DefaultComboBoxModel model, Collection dataCollection) {
            for (int i = 0; i < model.getSize(); i++){
                dataCollection.add(model.getElementAt(i));
            }
        }

        private void clearForm() {
            assigmentField.setText("");
            solutionField.setText("");
            timeField.setText("Tiempo");
            this.solutions.removeAllElements();
            this.nextProblems.removeAllElements();
            this.nextPossibleProblems.removeAllElements();
            problemList.clearSelection();
        }

        private void addSolution() {
            addSolution.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    solutions.addElement(new Solution(solutionField.getText(), currentProblem.getProblem()));
                    solutionField.setText("");
                    solutionField.transferFocus();
                }
            });
        }

        private void deleteProblem() {
            deleteProblem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //TODO completar deleteProblem
                }
            });
        }

        private void addProblem() {
            addProblem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    enabledForm(true);
                    clearForm();
                    currentProblem = new ProblemNode(new Problem("" ,new HashSet<Solution>()), new TreeMap<Integer, ProblemNode>());
                    contextChange();
                }
            });
        }

        public void loadInformation() {
            subjectName.setText(aLesson.getLessonInformation().getSubject());
            topicModel = new DefaultComboBoxModel<String>(aLesson.getLessonInformation().getTopics());
            topicList.setModel(topicModel);
        }

        public void loadLesson(){
            this.loadInformation();
            problems = new DefaultComboBoxModel<ProblemNode>(new Vector<ProblemNode>(aLesson.getIndexedProblems()));
            problemList.setModel(problems);
            firstProblemCombo.setModel(problems);

        }

        public void updateInformation(){
            String[] topics = new String[topicModel.getSize()];
            for (int i=0; i<topicModel.getSize(); i++){
                topics[i] = topicModel.getElementAt(i);
            }
            aLesson.setLessonInformation(new Information(subjectName.getText(), topics));
        }

        public void updateLesson(){
            ArrayList<ProblemNode> problemNodes = new ArrayList<ProblemNode>();
            this.modelToCollection(problems, problemNodes);
            aLesson.setIndexedProblems(problemNodes);
            this.updateInformation();
        }

    }

    private class Run implements Observer{

        private Problem currentProblem;
        DefaultCategoryDataset dataset;
        DefaultTableModel tableModel;
        TimerAction timerInterface = new TimerAction() {

            @Override
            public void onTick(long secondsLeft) {
                remainingTimeLabel.setText(String.valueOf(secondsLeft));
            }

            @Override
            public void onFinish() {
                remainingTimeLabel.setText("0");
                setEnabled(true);
            }
        };

        private Run() {
            setNextButton();
            setRunButton();
            setFirstProblemCombo();
            loadInformation();
            server.addObserver(this);
        }

        private void loadInformation() {
            //TODO solucionar problema de falta de informacion al inicio del server
            //TODO Test
            //aLesson.setLessonInformation(new Information("Laboratorio De Software", new String[]{"Tema 1", "tema 2", "tema 3"}));
            server.loadInformation(aLesson.getLessonInformation());
        }

        private void setCurrentProblem(Problem currentProblem) {
            this.currentProblem = currentProblem;
            problemAssigment.setText(currentProblem.getAssigment());
            setEnabled(false);
            setUpChart();
        }

        private void setEnabled(Boolean enabled) {
            nextButton.setEnabled(enabled);
            runButton.setEnabled(enabled);
            firstProblemCombo.setEnabled(enabled);
            tabbedPane1.setEnabled(enabled);
        }

        private void setFirstProblemCombo() {
            firstProblemCombo.setModel(editor.getProblems());
        }

        private void setNextButton() {
            nextButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (ServerManager.getInstance().getProblem() == null){
                        setCurrentProblem(server.next(timerInterface));
                    }
                }
            });
        }

        private void setRunButton(){

            runButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    setCurrentProblem(((ProblemNode) firstProblemCombo.getSelectedItem()).getProblem());
                    server.run((ProblemNode) firstProblemCombo.getSelectedItem(), aLesson.getLessonInformation(), timerInterface);

                }
            });
        }


        private Lesson getLesson() {
            return aLesson;
        }

        @Override
        public void update(Observable o, Object arg) {
            Par<Problem, Map<Solution, Integer>> statisticData = (Par<Problem, Map<Solution, Integer>>) arg;
            if (statisticData.getKey().equals(currentProblem)){
                for (Solution aSolution : statisticData.getValue().keySet()){
                    dataset.setValue(statisticData.getValue().get(aSolution), "Cantidad de Respuestas", aSolution.getDescription());
                }
            }
        }

        public void setUpChart(){
            dataset = new DefaultCategoryDataset();
            for (Solution aSolution : currentProblem.getSolutions()){
                dataset.setValue(0, "Cantidad de Respuestas", aSolution.getDescription());
            }
            resultsTable.setChart(ChartFactory.createBarChart("Resolucion del problema", "Cantidad de Respuestas", "Soluciones", dataset, PlotOrientation.VERTICAL, false, true, false));
            NumberAxis axis = (NumberAxis) resultsTable.getChart().getCategoryPlot().getRangeAxis();
            axis.setTickUnit(new NumberTickUnit(1D));
            axis.setRangeType(RangeType.POSITIVE);
            axis.setAutoRangeMinimumSize(10);
            resultsTable.validate();
        }


    }

    public void saveFileChooser(){
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showSaveDialog(mainPanel) == JFileChooser.APPROVE_OPTION) {
            exportLesson(resetFile(fileChooser.getSelectedFile()));
        }
    }

    public void openFileChooser(){
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(mainPanel) == JFileChooser.APPROVE_OPTION) {
            importLesson(fileChooser.getSelectedFile());
        }
    }

    private File resetFile(File file) {
        String path = file.getPath();
        file.delete();
        return new File(path);
    }

    private void exportLesson(File file) {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file));
            editor.updateLesson();
            outputStream.writeObject(aLesson);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private void importLesson(File file){
        try {
            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file));
            aLesson = (Lesson) inputStream.readObject();
            editor.loadLesson();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ClassNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public void setUpMenu(JFrame aFrame){
        JMenuBar menuBar = new JMenuBar();

        JMenu menu;
        JMenuItem menuItem;

        //Build the first menu.
        menu = new JMenu("A Menu");
        menu.setMnemonic(KeyEvent.VK_A);
        menu.getAccessibleContext().setAccessibleDescription(
                "The only menu in this program that has menu items");
        menuBar.add(menu);

        //a group of JMenuItems
        menuItem = new JMenuItem("Import",
                KeyEvent.VK_T);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_1, ActionEvent.ALT_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription(
                "Load++");
        menu.add(menuItem);
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openFileChooser();
            }
        });

        menuItem = new JMenuItem("Export",
                KeyEvent.VK_T);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_1, ActionEvent.ALT_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription(
                "Save++");
        menu.add(menuItem);
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveFileChooser();
            }
        });

        aFrame.setJMenuBar(menuBar);

    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("ServerUI");
        ServerUI serverUI = new ServerUI();
        frame.setContentPane(serverUI.mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        serverUI.setUpMenu(frame);
        frame.pack();
        frame.setVisible(true);
    }

}
