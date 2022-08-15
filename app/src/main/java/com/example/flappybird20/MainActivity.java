package com.example.flappybird20;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}

package com.manan.game;
// import androidx.appcompat.app.AppCompatActivity;
// import androidx.media2.player.MediaPlayer;    //1

        import com.badlogic.gdx.ApplicationAdapter;
        import com.badlogic.gdx.Gdx;
        import com.badlogic.gdx.graphics.Color;
        import com.badlogic.gdx.graphics.GL20;
        import com.badlogic.gdx.graphics.Texture;
        import com.badlogic.gdx.graphics.g2d.BitmapFont;
        import com.badlogic.gdx.graphics.g2d.SpriteBatch;
        import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
        import com.badlogic.gdx.math.Circle;
        import com.badlogic.gdx.math.Intersector;
        import com.badlogic.gdx.math.Rectangle;
        import com.badlogic.gdx.utils.ScreenUtils;
//import android.media.MediaPlayer;    //2

        import java.util.Random;

public class FlappyBird extends ApplicationAdapter {
    SpriteBatch batch;  // Sprite ek datatype hai jo info. hold karta hai regarding our texture, position etc.
    Texture background;   // Texture bhi ek tarah ki sprite hi hai.
    // ShapeRenderer shapeRenderer;
    Texture gameover;

    Texture[] birds;      // Texture can also be taken as an array.
    int flapState = 0;
    float birdY = 0;
    float velocity = 0;
    Circle birdCircle;
    int score = 0;
    int scoringTube = 0;
    BitmapFont font;

    int gameState = 0;
    float gravity = 2;
    Texture topTube;
    Texture bottomTube;
    float gap = 400;
    float maxTubeOffset;
    Random randomGenerator;
    float tubeVelocity = 4;
    int numberOfTubes = 4;
    float[] tubeX = new float[numberOfTubes];
    float[] tubeOffset = new float[numberOfTubes];
    float distanceBetweenTheTubes;
    Rectangle[] topTubeRectangles;
    Rectangle[] bottomTubeRectangles;

//	private MediaPlayer mediaPlayer;   // 3

    @Override
    public void create () {
        batch = new SpriteBatch();
        background = new Texture("bg.png");
        gameover = new Texture("gameover.png");
        // shapeRenderer = new ShapeRenderer();
        birdCircle = new Circle();

        birds = new Texture[2];    // Because we need 2 states of our bird, one with uppar vaale wings & one with niche vaale wings.
        birds[0] = new Texture("bird.png");
        birds[1] = new Texture("bird2.png");

        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(10);

        topTube = new Texture("toptube.png");
        bottomTube = new Texture(("bottomtube.png"));

        maxTubeOffset = Gdx.graphics.getHeight()/2 - gap/2 - 100;
        randomGenerator = new Random();
        distanceBetweenTheTubes = Gdx.graphics.getWidth() * 3/4;
        topTubeRectangles = new Rectangle[numberOfTubes];
        bottomTubeRectangles = new Rectangle[numberOfTubes];

        startGame();
    }

    public void startGame()
    {
        birdY = Gdx.graphics.getHeight()/2 - birds[flapState].getHeight()/2;
        for(int i = 0; i < numberOfTubes; i++){
            tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);
            tubeX[i] = Gdx.graphics.getWidth()/2 - topTube.getWidth()/2 + Gdx.graphics.getWidth() + i * distanceBetweenTheTubes;

            topTubeRectangles[i] = new Rectangle();
            bottomTubeRectangles[i] = new Rectangle();
        }
    }

    @Override
    public void render () {   // Does continuous looping to produce an effect of animation.

        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        // getWidth() aur getHeight() functions se hamara background poori mobile screen par stretch ho jayega.

        if(gameState == 1){

            if(tubeX[scoringTube] < Gdx.graphics.getWidth() / 2)
            {
                Gdx.app.log("Score", String.valueOf(score));
                score++;
                if(scoringTube < numberOfTubes - 1)
                {
                    scoringTube++;
                }
                else {
                    scoringTube = 0;
                }
            }

            if(Gdx.input.justTouched())
            {
                velocity = -22.5f;     // Since, birdY = birdY - velocity, hence (-) X (-) = (+)

            }

            for(int i = 0; i < numberOfTubes; i++) {
                if(tubeX[i] < -topTube.getWidth())
                {
                    tubeX[i] += numberOfTubes * distanceBetweenTheTubes;
                    tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);
                }
                else {
                    tubeX[i] = tubeX[i] - tubeVelocity;


                }
                batch.draw(topTube, tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i]);
                batch.draw(bottomTube, tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i]);

                topTubeRectangles[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i], topTube.getWidth(), topTube.getHeight());
                bottomTubeRectangles[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i], bottomTube.getWidth(), bottomTube.getHeight());
            }
            if(birdY > 0 && birdY < Gdx.graphics.getHeight()) {
                velocity = velocity + gravity;
                birdY -= velocity;
            }
            else{
                gameState = 2;     // 2 is the game over state
            }
        }
        else if(gameState == 0){
            if(Gdx.input.justTouched())
            {
//				Gdx.app.log("Touched", "Yep !");    // This was just to check whether finger touch on phone works or not.
                gameState = 1;
            }
        }
        else if (gameState == 2)
        {
            batch.draw(gameover, Gdx.graphics.getWidth() / 2 - gameover.getWidth() / 2, Gdx.graphics.getHeight() / 2 - gameover.getHeight());

            if(Gdx.input.justTouched())
            {
//				Gdx.app.log("Touched", "Yep !");    // This was just to check whether finger touch on phone works or not.
                gameState = 1;
                startGame();
                score = 0;
                scoringTube = 0;
                velocity = 0;
            }
        }

        if(flapState == 0){
            flapState = 1;
        }
        else{
            flapState = 0;
        }

        batch.draw(birds[flapState], Gdx.graphics.getWidth()/2 - birds[flapState].getWidth()/2, birdY);
        font.draw(batch, String.valueOf(score), 100, 200);

        batch.end();

        birdCircle.set(Gdx.graphics.getWidth()/2, birdY + birds[flapState].getHeight()/2, birds[flapState].getWidth()/2);

//		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//		shapeRenderer.setColor(Color.RED);
//		shapeRenderer.circle(birdCircle.x, birdCircle.y, birdCircle.radius);

        for(int i = 0; i < numberOfTubes; i++)
        {
            // shapeRenderer.rect(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i], topTube.getWidth(), topTube.getHeight());
            // shapeRenderer.rect(tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i], bottomTube.getWidth(), bottomTube.getHeight());

            if(Intersector.overlaps(birdCircle, topTubeRectangles[i]) || Intersector.overlaps(birdCircle, bottomTubeRectangles[i]))
            {
//				 Gdx.app.log("Collision", "Yes!");  // To keep a track of where collisions are happening
                gameState = 2;     // Yeh game over vaali state hai
            }
        }
        // shapeRenderer.end();
    }
}