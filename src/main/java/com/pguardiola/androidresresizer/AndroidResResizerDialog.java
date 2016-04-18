/*
 * Copyright (C) 2016 Pablo Guardiola Sánchez.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pguardiola.androidresresizer;

import com.intellij.ide.util.BrowseFilesListener;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComponentWithBrowseButton;
import com.intellij.openapi.ui.TextComponentAccessor;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.wm.WindowManager;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

public class AndroidResResizerDialog extends JDialog implements Popup {
    private static final String POPUP_TITLE = "Android Res Resizer";
    private JPanel contentPane;
    private JButton buttonCancel;
    private JButton buttonOK;
    private TextFieldWithBrowseButton filePathTextField;
    private JComboBox densitiesComboBox;
    private Container relativeContainer;
    private AndroidResResizerPresenter popupPresenter;

    public AndroidResResizerDialog(Project environment) {
        popupPresenter = new AndroidResResizerPresenterImpl(this);

        relativeContainer = WindowManager.getInstance().getFrame(environment);
        setTitle(POPUP_TITLE);
        setContentPane(contentPane);
        setMinimumSize(new Dimension(480, 140));
        setModal(true);

        getRootPane().setDefaultButton(buttonOK);

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                popupPresenter.onCancelClicked();
            }
        });

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        filePathTextField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                validateFilePath();
            }
        });

        filePathTextField.getTextField().addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                validateFilePath();
            }
        });
        ComponentWithBrowseButton.BrowseFolderActionListener<JTextField> browserFolderActionListener =
                new ComponentWithBrowseButton.BrowseFolderActionListener<>("Select File",
                        "Select the image to generate assets in the required densities", filePathTextField,
                        environment, BrowseFilesListener.SINGLE_FILE_DESCRIPTOR,
                        TextComponentAccessor.TEXT_FIELD_WHOLE_TEXT);
        filePathTextField.addBrowseFolderListener(environment, browserFolderActionListener);

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                popupPresenter.onCancelClicked();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
                                               public void actionPerformed(ActionEvent e) {
                                                   popupPresenter.onCancelClicked();
                                               }
                                           }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    @Override
    public void display() {
        refresh();
        setVisible(true);
    }

    @Override
    public void close() {
        dispose();
    }

    @Override
    public void establishOkVisibility(boolean isEnabled) {
        buttonOK.setEnabled(isEnabled);
    }

    @Override
    public void refresh() {
        pack();
        setLocationRelativeTo(relativeContainer);
    }

    private void onOK() {
        String enteredPath = filePathTextField.getChildComponent().getText();
        String selectedBaseDensity = (String) densitiesComboBox.getSelectedItem();

        ParamsDTO chosenParams = new ParamsDTO();
        chosenParams.setPath(enteredPath);
        chosenParams.setBaseDensity(selectedBaseDensity);

        popupPresenter.onResizeClicked(chosenParams);
    }

    private void validateFilePath() {
        String entered = filePathTextField.getChildComponent().getText();

        popupPresenter.validatePath(entered);
    }
}
