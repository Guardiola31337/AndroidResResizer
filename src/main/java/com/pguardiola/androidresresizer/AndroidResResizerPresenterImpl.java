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

import java.io.IOException;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;

public class AndroidResResizerPresenterImpl implements AndroidResResizerPresenter {
    private final Popup popup;

    public AndroidResResizerPresenterImpl(Popup popup) {
        this.popup = popup;
    }

    @Override
    public void onCancelClicked() {
        popup.close();
    }

    @Override
    public void onResizeClicked(ParamsDTO params) {
        String path = params.getPath();
        String baseDensity = params.getBaseDensity();

        System.out.println("File path:" + path);
        System.out.println("Base density:" + baseDensity);

        executeResize(path);

        popup.close();
    }

    @Override
    public void validatePath(String path) {
        boolean isValid = !path.isEmpty();
        popup.establishOkVisibility(isValid);
        popup.refresh();
    }

    private void executeResize(String resource) {
        String myPath = "/usr/local/bin";

        ConvertCmd cmd = new ConvertCmd();
        cmd.setSearchPath(myPath);

        // create the operation, add images and operators/options
        IMOperation op = new IMOperation();
        op.addImage(resource);
        op.filter("Cubic");
        op.set("option:filter:blur").addRawArgs("0.5");
        op.resize().addRawArgs("23%");
        op.addImage("/Users/soporte/IdeaProjects/AndroidResResizer/foo_small_xxhdpi.jpg");

        // execute the operation
        try {
            cmd.run(op);
        } catch (IOException io) {
            io.printStackTrace();
        } catch (InterruptedException interrupted) {
            interrupted.printStackTrace();
        } catch (IM4JavaException im4java) {
            im4java.printStackTrace();
        }
    }
}
