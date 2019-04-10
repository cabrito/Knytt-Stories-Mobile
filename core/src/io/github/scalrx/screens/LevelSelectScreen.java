package io.github.scalrx.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FitViewport;

import io.github.scalrx.KnyttStories;
import io.github.scalrx.gui.GuiLevelButton;

/***************************************************************************************************
 * Knytt Stories Mobile      (https://www.github.com/scalrx/knytt-stories-mobile)
 * LevelSelectScreen.java
 * Created by: scalr at 11:40 AM, 4/9/19
 *
 * Used for selecting which level the user wishes to play.
 *
 **************************************************************************************************/
public class LevelSelectScreen implements Screen {

    private final KnyttStories game;
    private FitViewport viewport;
    private OrthographicCamera camera;
    private Stage stage;

    public LevelSelectScreen(final KnyttStories game) {

        // Prepare our variables
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, KnyttStories.V_WIDTH,KnyttStories.V_HEIGHT);
        viewport = new FitViewport(KnyttStories.V_WIDTH, KnyttStories.V_HEIGHT, camera);

        // Load our assets we'll use
        loadAssets();
        Texture guiBtnLevel = game.assetManager.get("System/Gui_btn_level.png", Texture.class);
        Texture juniIcon    = game.assetManager.get("Data/Objects/Juni/Juni0.png", Texture.class);

        // Prepare the stage for level selection
        stage = new Stage(viewport, game.batch);
        Gdx.input.setInputProcessor(stage);

        // Create the look for each element in the scrollpane
        Skin levelButton = new Skin();
        //final float ICON_SIZE = 30;
        levelButton.add("gui-button-level", guiBtnLevel);
        levelButton.add("gui-button-level", game.assetManager.get("smallFont.ttf", BitmapFont.class));
        levelButton.add("description-font", game.assetManager.get("smallGrayFont.ttf", BitmapFont.class));

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = levelButton.newDrawable("gui-button-level");
        textButtonStyle.down = levelButton.newDrawable("gui-button-level", 0.0f,0.0f,0.0f,0.5f);
        textButtonStyle.font = levelButton.getFont("gui-button-level");
        levelButton.add("default", textButtonStyle);

        GuiLevelButton levelBtn = new GuiLevelButton(juniIcon,"The Machine (Nifflas)", "testestest", levelButton);

        /*TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = levelButton.newDrawable("gui-button-level");
        textButtonStyle.down = levelButton.newDrawable("gui-button-level", 0.0f,0.0f,0.0f,0.5f);
        textButtonStyle.font = levelButton.getFont("gui-button-level");

        TextButton levelBtn = new TextButton("The Machine (Nifflas)", textButtonStyle);
        levelBtn.clearChildren();                     // Reorganize the layout of the ImageTextButton
        levelBtn.left().padLeft(2f);
        levelBtn.add(new Image(juniIcon)).size(ICON_SIZE).padRight(2f);
        levelBtn.add(levelBtn.getLabel());*/

        // Make the table used in the scrollpane
        Table table = new Table();
        table.add(levelBtn);
        ScrollPane scrollPane = new ScrollPane(table);
        scrollPane.setPosition(0f,KnyttStories.V_HEIGHT/2);
        scrollPane.setSize(300,200);

        stage.addActor(scrollPane);

    }

    private void loadAssets() {
        // Load internal resources
        game.assetManager.setLoader(Texture.class, new TextureLoader(new InternalFileHandleResolver()));
        game.assetManager.load("System/Gui_btn_level.png", Texture.class);
        game.assetManager.load("Data/Objects/Juni/Juni0.png", Texture.class);
        game.assetManager.finishLoading();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        // Clear the screen for drawing the next frame
        Gdx.gl.glClearColor(1f, 1f, 1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Update the camera for any movement that may occur
        viewport.getCamera().update();
        game.batch.setProjectionMatrix(viewport.getCamera().combined);

        // Begin drawing our GUI to the screen
        game.batch.begin();

        // End all drawing from the SpriteBatch
        game.batch.end();
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width,height,true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        game.assetManager.unload("System/Gui_btn_level.png");
        game.assetManager.unload("Data/Objects/Juni/Juni0.png");
        stage.dispose();
    }
}
