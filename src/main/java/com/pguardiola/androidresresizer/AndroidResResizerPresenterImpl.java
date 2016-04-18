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

public class AndroidResResizerPresenterImpl implements AndroidResResizerPresenter {
    private final Popup popup;
    private Resizer resizer;

    public AndroidResResizerPresenterImpl(Popup popup) {
        this.popup = popup;
    }

    @Override
    public void onCancelClicked() {
        popup.close();
    }

    @Override
    public void onResizeClicked(ParamsDTO params) {
        String image = params.getPath();
        String density = params.getBaseDensity();

        System.out.println("File path:" + image);
        System.out.println("Base density:" + density);

        Resizer resizer = obtainResizer();
        resizer.resize(image);

        popup.close();
    }

    @Override
    public void validatePath(String path) {
        boolean isValid = !path.isEmpty();
        popup.establishOkVisibility(isValid);
        popup.refresh();
    }

    private Resizer obtainResizer() {
        if (resizer == null) {
            resizer = new Resizer();
        }

        return resizer;
    }
}
