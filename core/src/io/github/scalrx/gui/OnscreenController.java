package io.github.scalrx.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FitViewport;

import io.github.scalrx.KnyttStories;

/***************************************************************************************************
 * Knytt Stories Mobile      (https://www.github.com/scalrx/knytt-stories-mobile)
 * FILE NAME GOES HERE.java
 * Created by: scalr at 7:17 PM, 4/2/19
 *
 * FILE DESCRIPTION GOES HERE.
 *
 **************************************************************************************************/
public class OnscreenController {
    private FitViewport viewport;
    private OrthographicCamera camera;
    private Stage stage;
    private boolean isLeftPressed, isRightPressed, isUpPressed, isDownPressed;

    public OnscreenController(Batch batch) {
        // Set the stage, figuratively and literally
        camera = new OrthographicCamera();
        viewport = new FitViewport(KnyttStories.V_WIDTH, KnyttStories.V_HEIGHT, camera);
        stage = new Stage(viewport, batch);
        Gdx.input.setInputProcessor(stage);

        // Initially, nothing will be detected as pressed.
        isLeftPressed = isRightPressed = isUpPressed = isDownPressed = false;

        // Build the stage for our onscreen controller
        Table table = new Table();
        table.left().bottom();

        Image upImg = new Image(new Texture("System/icons/flatDark25.png"));
        upImg.setSize(50, 50);
        upImg.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                isUpPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                isUpPressed = false;
            }
        });

        Image downImg = new Image(new Texture("System/icons/flatDark26.png"));
        downImg.setSize(50, 50);
        downImg.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                isDownPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                isDownPressed = false;
            }
        });

        Image rightImg = new Image(new Texture("System/icons/flatDark24.png"));
        rightImg.setSize(50, 50);
        rightImg.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                isRightPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                isRightPressed = false;
            }
        });

        Image leftImg = new Image(new Texture("System/icons/flatDark23.png"));
        leftImg.setSize(50, 50);
        leftImg.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                isLeftPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                isLeftPressed = false;
            }
        });

        table.add();
        table.add(upImg).size(upImg.getWidth(), upImg.getHeight());
        table.add();
        table.row().pad(5, 5, 5, 5);
        table.add(leftImg).size(leftImg.getWidth(), leftImg.getHeight());
        table.add();
        table.add(rightImg).size(rightImg.getWidth(), rightImg.getHeight());
        table.row().padBottom(5);
        table.add();
        table.add(downImg).size(downImg.getWidth(), downImg.getHeight());
        table.add();

        stage.addActor(table);
    }

    // Getters
    public boolean isLeftPressed() {
        return isLeftPressed;
    }

    public boolean isRightPressed() {
        return isRightPressed;
    }

    public boolean isUpPressed() {
        return isUpPressed;
    }

    public boolean isDownPressed() {
        return isDownPressed;
    }

    // Resetter
    public void resetTouch() {
        isLeftPressed = isRightPressed = isUpPressed = isDownPressed = false;
    }

    // For screen resizing
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    public void draw() {
        stage.draw();
    }

    public void dispose() {
        stage.dispose();
    }
}
