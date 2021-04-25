package ru.myitschool.sunchess;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChessGame extends ApplicationAdapter {
	// base host1674609_sunchessbase
	// user host1674609_sunchessuser
	// pass pentagon2024boom
	public static final int SCR_WIDTH = 720/4*3, SCR_HEIGHT = 1280/4*3;
	public static final int WHITE = 0, BLACK = 1;
	public static final int OUR_SIDE = 0, OTHER_SIDE = 1;
	public static final int PAWN=0, BISHOP=1, HORSE=2, ROOK=3, QUEEN=4, KING=5;

	SpriteBatch batch;
	OrthographicCamera camera;
	Vector3 touch;
	MyInputProcessor processor;

	Texture imgChessAtlas;
	TextureRegion[] imgCell = new TextureRegion[2];
	TextureRegion[][] imgFigure = new TextureRegion[2][6];
	Texture imgButtons;
	TextureRegion[] imgButtonCreate = new TextureRegion[2];

	static int paddingBottom = (SCR_HEIGHT-SCR_WIDTH)/2;
	Cell[][] board = new Cell[8][8];
	static float size = SCR_WIDTH/8f;
	Figure[][] figure = new Figure[2][16];
	int[] fig = {ROOK, HORSE, BISHOP, QUEEN, KING, BISHOP, HORSE, ROOK};  // расстановка фигур на доске

	ChessButton btnCreate;

	long lastTime, timeInterval = 1000, count;
	boolean isLowerFPS = true;
	int ourColor, otherColor;

	ChessData chessData;
	Retrofit retrofit;
	ChessAPI chessAPI;

	@Override
	public void create () {
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		touch = new Vector3();
		camera.setToOrtho(false, SCR_WIDTH, SCR_HEIGHT);

		processor=new MyInputProcessor();
		Gdx.input.setInputProcessor(processor);

		imgChessAtlas = new Texture("chessatlas.png");
		for(int i=0; i<2; i++) imgCell[i] = new TextureRegion(imgChessAtlas, 6*300, i*300, 300, 300);
		for(int i=0; i<2; i++)
			for(int j=0; j<6; j++)
				imgFigure[i][j] = new TextureRegion(imgChessAtlas, j*300, i*300, 300, 300);
		imgButtons = new Texture("buttons.png");
		for(int i=0; i<2; i++) imgButtonCreate[i] = new TextureRegion(imgButtons, 0, i*140, 400, 140);

		int k = 1;
		for(int j=0; j<8; j++, k++)
			for(int i=0; i<8; i++)
				board[i][j] = new Cell(i, j, k++ % 2, null);

		btnCreate = new ChessButton(10, 10, 150, 50);

		retrofit = new Retrofit.Builder().
				baseUrl("https://sunchess.tibedox.ru").
				addConverterFactory(GsonConverterFactory.create()).
				build();
		chessAPI = retrofit.create(ChessAPI.class);

		arrangement(MathUtils.random(WHITE, BLACK));
	}

	void arrangement(int color){
		ourColor = color;
		if(ourColor == WHITE) otherColor = BLACK;
		else {
			otherColor = WHITE;
			fig[3] = KING;
			fig[4] = QUEEN;
		}

		for(int i=0; i<8; i++){
			figure[OUR_SIDE][i] = new Figure(PAWN, ourColor, i, 1, OUR_SIDE);
			board[i][1].figure = figure[WHITE][i];
			figure[OUR_SIDE][i+8] = new Figure(fig[i], ourColor, i, 0, OUR_SIDE);
			board[i][0].figure = figure[WHITE][i+8];

			figure[OTHER_SIDE][i] = new Figure(PAWN, otherColor, i, 6, OTHER_SIDE);
			board[i][6].figure = figure[BLACK][i];
			figure[OTHER_SIDE][i+8] = new Figure(fig[i], otherColor, i, 7, OTHER_SIDE);
			board[i][7].figure = figure[BLACK][i+8];
		}
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0.3f, 0, 0.4f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();

	/*	if(TimeUtils.millis() > lastTime+timeInterval){
			System.out.println("№ "+count);
			lastTime = TimeUtils.millis();
			count = 0;
		}
		++count;*/

		if(isLowerFPS) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		batch.draw(imgButtonCreate[btnCreate.state], btnCreate.x, btnCreate.y, btnCreate.width, btnCreate.height);
		for(int j=0; j<8; j++)
			for(int i=0; i<8; i++)
				batch.draw(imgCell[board[i][j].color], i*size, j*size+paddingBottom, size, size);

		for(int i=0; i<2; i++)
			for(int j=0; j<16; j++)
				batch.draw(imgFigure[figure[i][j].color][figure[i][j].type], figure[i][j].x, figure[i][j].y, size, size);

		for(int i=0; i<2; i++)
			for(int j=0; j<16; j++)
				if(figure[i][j].condition==Figure.MOVE)
					batch.draw(imgFigure[figure[i][j].color][figure[i][j].type], figure[i][j].x, figure[i][j].y, size, size);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		imgChessAtlas.dispose();
	}

	class MyInputProcessor implements InputProcessor{

		@Override
		public boolean keyDown(int keycode) {
			return false;
		}

		@Override
		public boolean keyUp(int keycode) {
			return false;
		}

		@Override
		public boolean keyTyped(char character) {
			return false;
		}

		@Override
		public boolean touchDown(int screenX, int screenY, int pointer, int button) {
			touch.set(screenX, screenY, 0);
			camera.unproject(touch);
			if(btnCreate.isHit(touch.x, touch.y)) {
				btnCreate.press();
			}

			for(int i=0; i<2; i++)
				for(int j=0; j<16; j++)
					if (figure[i][j].isHit(touch.x/size, (touch.y-paddingBottom)/size))
						if(figure[i][j].condition == Figure.FREE)
							figure[i][j].condition = Figure.MOVE;
			isLowerFPS = false;
			return false;
		}

		@Override
		public boolean touchUp(int screenX, int screenY, int pointer, int button) {
			touch.set(screenX, screenY, 0);
			camera.unproject(touch);

			if(btnCreate.isHit(touch.x, touch.y)) {
				btnCreate.realise();
				chessAPI.createNetGame("create", "Bunny").enqueue(new Callback<ChessData>() {
					@Override
					public void onResponse(Call<ChessData> call, Response<ChessData> response) {
						chessData = response.body();
						arrangement(chessData.color);
					}

					@Override
					public void onFailure(Call<ChessData> call, Throwable t) {
					}
				});
			}

			for(int i=0; i<2; i++)
				for(int j=0; j<16; j++)
					if(figure[i][j].condition == Figure.MOVE)
						figure[i][j].put(touch.x, touch.y, board);
			isLowerFPS = true;
			return false;
		}

		@Override
		public boolean touchDragged(int screenX, int screenY, int pointer) {
			touch.set(screenX, screenY, 0);
			camera.unproject(touch);
			for(int i=0; i<2; i++)
				for(int j=0; j<16; j++)
					if(figure[i][j].condition == Figure.MOVE)
						figure[i][j].move(touch.x, touch.y);
			return false;
		}

		@Override
		public boolean mouseMoved(int screenX, int screenY) {
			return false;
		}

		@Override
		public boolean scrolled(float amountX, float amountY) {
			return false;
		}
	}
}
