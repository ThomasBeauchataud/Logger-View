package com.github.ffcfalcos.logger.view;

import com.github.ffcfalcos.logger.trace.Rule;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.util.Callback;

import java.util.Collections;

@SuppressWarnings("Duplicates")
public class RulesManager {

    private TableView<Rule> activeRulesView;
    private TableView<Rule> passiveRulesView;
    private ConfigManager configManager;

    public RulesManager(ConfigManager configManager) {
        this.configManager = configManager;
        activeRulesView = createRuleTableView();
        if(configManager.getActiveRulesStorageHandler() != null) {
            activeRulesView.getItems().addAll(configManager.getActiveRulesStorageHandler().getRules());
        }
        activeRulesView.setMaxHeight(300);
        GridPane.setHgrow(activeRulesView, Priority.ALWAYS);
        passiveRulesView = createRuleTableView();
        if(configManager.getActiveRulesStorageHandler() != null) {
            passiveRulesView.getItems().addAll(configManager.getPassiveRulesStorageHandler().getRules());
        }
        passiveRulesView.setMaxHeight(300);
        GridPane.setHgrow(passiveRulesView, Priority.ALWAYS);
        addActiveRulesButtons();
        addPassiveRulesButtons();
        addDeleteActiveButtons();
        addDeletePassiveButtons();
    }

    public TableView<Rule> getActiveRulesView() {
        return activeRulesView;
    }

    public TableView<Rule> getPassiveRulesView() {
        return passiveRulesView;
    }

    public void reloadRules() {
        activeRulesView.getItems().clear();
        passiveRulesView.getItems().clear();
        if(configManager.getActiveRulesStorageHandler() != null) {
            activeRulesView.getItems().addAll(configManager.getActiveRulesStorageHandler().getRules());
        }
        passiveRulesView.getItems().addAll(configManager.getPassiveRulesStorageHandler().getRules());
        passiveRulesView.refresh();
        activeRulesView.refresh();
    }

    private void addPassiveRulesButtons() {
        TableColumn<Rule, Void> colBtn = new TableColumn<>("");
        Callback<TableColumn<Rule, Void>, TableCell<Rule, Void>> cellFactory = new Callback<TableColumn<Rule, Void>, TableCell<Rule, Void>>() {
            @Override
            public TableCell<Rule, Void> call(final TableColumn<Rule, Void> param) {
                return new TableCell<Rule, Void>() {
                    private final Button btn = new Button("Activate");
                    {
                        btn.setOnAction((ActionEvent event) -> {
                            Rule rule = getTableView().getItems().get(getIndex());
                            configManager.getActiveRulesStorageHandler().addRules(Collections.singletonList(rule));
                            configManager.getPassiveRulesStorageHandler().removeRules(Collections.singletonList(rule));
                            passiveRulesView.getItems().remove(rule);
                            activeRulesView.getItems().add(rule);
                        });
                    }
                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
            }
        };
        colBtn.setCellFactory(cellFactory);
        passiveRulesView.getColumns().add(colBtn);
    }

    private void addDeletePassiveButtons() {
        TableColumn<Rule, Void> colBtn = new TableColumn<>("");
        Callback<TableColumn<Rule, Void>, TableCell<Rule, Void>> cellFactory = new Callback<TableColumn<Rule, Void>, TableCell<Rule, Void>>() {
            @Override
            public TableCell<Rule, Void> call(final TableColumn<Rule, Void> param) {
                return new TableCell<Rule, Void>() {
                    private final Button btn = new Button("Delete");
                    {
                        btn.setOnAction((ActionEvent event) -> {
                            Rule rule = getTableView().getItems().get(getIndex());
                            configManager.getPassiveRulesStorageHandler().removeRules(Collections.singletonList(rule));
                            passiveRulesView.getItems().remove(rule);
                        });
                    }
                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
            }
        };
        colBtn.setCellFactory(cellFactory);
        passiveRulesView.getColumns().add(colBtn);
    }

    private void addDeleteActiveButtons() {
        TableColumn<Rule, Void> colBtn = new TableColumn<>("");
        Callback<TableColumn<Rule, Void>, TableCell<Rule, Void>> cellFactory = new Callback<TableColumn<Rule, Void>, TableCell<Rule, Void>>() {
            @Override
            public TableCell<Rule, Void> call(final TableColumn<Rule, Void> param) {
                return new TableCell<Rule, Void>() {
                    private final Button btn = new Button("Delete");
                    {
                        btn.setOnAction((ActionEvent event) -> {
                            Rule rule = getTableView().getItems().get(getIndex());
                            configManager.getActiveRulesStorageHandler().removeRules(Collections.singletonList(rule));
                            activeRulesView.getItems().remove(rule);
                        });
                    }
                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
            }
        };
        colBtn.setCellFactory(cellFactory);
        activeRulesView.getColumns().add(colBtn);
    }

    private void addActiveRulesButtons() {
        TableColumn<Rule, Void> colBtn = new TableColumn<>("");
        Callback<TableColumn<Rule, Void>, TableCell<Rule, Void>> cellFactory = new Callback<TableColumn<Rule, Void>, TableCell<Rule, Void>>() {
            @Override
            public TableCell<Rule, Void> call(final TableColumn<Rule, Void> param) {
                return new TableCell<Rule, Void>() {
                    private final Button btn = new Button("Deactivate");
                    {
                        btn.setOnAction((ActionEvent event) -> {
                            Rule rule = getTableView().getItems().get(getIndex());
                            configManager.getPassiveRulesStorageHandler().addRules(Collections.singletonList(rule));
                            configManager.getActiveRulesStorageHandler().removeRules(Collections.singletonList(rule));
                            activeRulesView.getItems().remove(rule);
                            passiveRulesView.getItems().add(rule);
                        });
                    }
                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
            }
        };
        colBtn.setCellFactory(cellFactory);
        activeRulesView.getColumns().add(colBtn);
    }

    private TableView<Rule> createRuleTableView() {
        TableColumn<Rule, String> column1 = new TableColumn<>("Class");
        column1.setCellValueFactory(new PropertyValueFactory<>("className"));
        TableColumn<Rule, String> column2 = new TableColumn<>("Method");
        column2.setCellValueFactory(new PropertyValueFactory<>("methodName"));
        TableColumn<Rule, String> column3 = new TableColumn<>("Entry");
        column3.setCellValueFactory(new PropertyValueFactory<>("entry"));
        TableColumn<Rule, String> column4 = new TableColumn<>("PersistingHandler");
        column4.setCellValueFactory(new PropertyValueFactory<>("persistingHandlerClass"));
        TableColumn<Rule, String> column5 = new TableColumn<>("FormatterHandler");
        column5.setCellValueFactory(new PropertyValueFactory<>("formatterHandlerClass"));
        TableColumn<Rule, String> column6 = new TableColumn<>("Context");
        column6.setCellValueFactory(new PropertyValueFactory<>("context"));
        TableView<Rule> tableView = new TableView<>();
        tableView.getColumns().add(column1);
        tableView.getColumns().add(column2);
        tableView.getColumns().add(column3);
        tableView.getColumns().add(column4);
        tableView.getColumns().add(column5);
        tableView.getColumns().add(column6);
        return tableView;
    }
}
