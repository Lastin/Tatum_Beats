package com.tatum.states;

import static com.tatum.handlers.B2DVars.PPM;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.tatum.entities.B2DSprite;
import com.tatum.handlers.Animation;
import com.tatum.handlers.B2DVars;
import com.tatum.handlers.Background;
import com.tatum.handlers.GameButton;
import com.tatum.handlers.GameStateManager;

public class Menu extends GameState {
    private boolean debug = false;
    private Background bg;
    private Animation bunnyAnimation;
    private GameButton playButton;
    private World world;
    private Box2DDebugRenderer b2dRenderer;
    private Array<B2DSprite> blocks;

    public Menu(GameStateManager gsm) {
        super(gsm);

        loadContent();

        Texture menu = resources.getTexture("menu2");
        bg = new Background(game, new TextureRegion(menu), cam, 1f);
        bg.setVector(-20, 0);
        Texture bunny = resources.getTexture("bunny");
        TextureRegion[] bunnySprite = TextureRegion.split(bunny, 32, 32)[0];
        bunnyAnimation = new Animation(bunnySprite, 1/12f);
        Texture hud = resources.getTexture("hud2");
        playButton = new GameButton(resources, new TextureRegion(hud, 0, 34, 58, 27), 160, 100, cam);
        cam.setToOrtho(false, game.getWidth(), game.getHeight());

        world = new World(new Vector2(0, -9.8f * 5), true);
        //world = new World(new Vector2(10, 10), true);
        b2dRenderer = new Box2DDebugRenderer();

        createTitleBodies();

    }

    private void createTitleBodies() {
        int[][] spellBlock = {
                {1, 1, 1, 1, 1, 0, 0, 0, 1, 0, 0, 0, 1, 1, 1, 1, 1, 0, 1, 0, 0, 0, 1, 0, 1, 1, 0, 1, 1},
                {0, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1},
                {0, 0, 1, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1},
                {0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1},
                {0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 1, 1, 1, 1, 0, 1, 0, 0, 0, 1}
        };
        int[][] spellBunny = {
                {1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 0, 0, 1, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1},
                {1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0},
                {1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 1, 1, 0},
                {1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 1, 1, 1, 0},
        };
        // top platform
        BodyDef tpbdef = new BodyDef();
        tpbdef.type = BodyType.StaticBody;
        tpbdef.position.set(160 / PPM, 180 / PPM);
        Body tpbody = world.createBody(tpbdef);
        PolygonShape tpshape = new PolygonShape();
        tpshape.setAsBox(120 / PPM, 1 / PPM);
        FixtureDef tpfdef = new FixtureDef();
        tpfdef.shape = tpshape;
        tpfdef.filter.categoryBits = B2DVars.BIT_TOP_PLATFORM;
        tpfdef.filter.maskBits = B2DVars.BIT_TOP_BLOCK;
        tpbody.createFixture(tpfdef);
        tpshape.dispose();

        // bottom platform
        BodyDef bpbdef = new BodyDef();
        bpbdef.type = BodyType.StaticBody;
        bpbdef.position.set(160 / PPM, 130 / PPM);
        Body bpbody = world.createBody(bpbdef);
        PolygonShape bpshape = new PolygonShape();
        bpshape.setAsBox(120 / PPM, 1 / PPM);
        FixtureDef bpfdef = new FixtureDef();
        bpfdef.shape = bpshape;
        bpfdef.filter.categoryBits = B2DVars.BIT_BOTTOM_PLATFORM;
        bpfdef.filter.maskBits = B2DVars.BIT_BOTTOM_BLOCK;
        bpbody.createFixture(bpfdef);
        bpshape.dispose();

        Texture tex = resources.getTexture("hud2");
        TextureRegion[] blockSprites = new TextureRegion[3];
        for(int i = 0; i < blockSprites.length; i++) {
            blockSprites[i] = new TextureRegion(tex, 58 + i * 5, 34, 5, 5);
        }
        blocks = new Array<B2DSprite>();

        for(int row = 0; row < 5; row++) {
            for(int col = 0; col < 29; col++) {
                BodyDef tbbdef = new BodyDef();
                tbbdef.type = BodyType.DynamicBody;
                tbbdef.fixedRotation = true;
                tbbdef.position.set((62 + col * 6 + col) / PPM, (270 - row * 6  + row) / PPM);
                Body tbbody = world.createBody(tbbdef);
                PolygonShape tbshape = new PolygonShape();
                tbshape.setAsBox(2f / PPM, 2f / PPM);
                FixtureDef tbfdef = new FixtureDef();
                tbfdef.shape = tbshape;
                tbfdef.filter.categoryBits = B2DVars.BIT_TOP_BLOCK;
                tbfdef.filter.maskBits = B2DVars.BIT_TOP_PLATFORM | B2DVars.BIT_TOP_BLOCK;
                tbbody.createFixture(tbfdef);
                tbshape.dispose();
                if(spellBlock[row][col] == 1) {
                    B2DSprite sprite = new B2DSprite(tbbody, resources);
                    sprite.setAnimation(blockSprites[MathUtils.random(2)], 0);
                    blocks.add(sprite);
                }
            }
        }

        // bottom blocks
        for(int row = 0; row < 5; row++) {
            for(int col = 0; col < 29; col++) {
                BodyDef bbbdef = new BodyDef();
                bbbdef.type = BodyType.DynamicBody;
                bbbdef.fixedRotation = true;
                bbbdef.position.set((62 + col * 6 + col) / PPM, (270 - row * 6 + row) / PPM);
                Body bbbody = world.createBody(bbbdef);
                PolygonShape bbshape = new PolygonShape();
                bbshape.setAsBox(2f / PPM, 2f / PPM);
                FixtureDef bbfdef = new FixtureDef();
                bbfdef.shape = bbshape;
                bbfdef.filter.categoryBits = B2DVars.BIT_BOTTOM_BLOCK;
                bbfdef.filter.maskBits = B2DVars.BIT_BOTTOM_PLATFORM | B2DVars.BIT_BOTTOM_BLOCK;
                bbbody.createFixture(bbfdef);
                bbshape.dispose();
                if(spellBunny[row][col] == 1) {
                    B2DSprite sprite = new B2DSprite(bbbody, resources);
                    sprite.setAnimation(blockSprites[MathUtils.random(2)], 0);
                    blocks.add(sprite);
                }
            }
        }
    }

    public void handleInput() {
        if (playButton.isClicked()) {
            resources.getSound("crystal").play();
            gsm.setState(new Play(gsm));
        }
    }

    public void update(float dt) {
        handleInput();
        world.step(dt / 5, 8, 3);
        bg.update(dt);
        bunnyAnimation.update(dt);
        playButton.update(dt);

    }

    public void render() {
        sb.setProjectionMatrix(cam.combined);
        bg.render(sb);
        playButton.render(sb);
        sb.begin();
        sb.draw(bunnyAnimation.getFrame(), 146, 31);
        sb.end();
        if (debug) {
            cam.setToOrtho(false, game.getWidth() / PPM, game.getHeight() / PPM);
            b2dRenderer.render(world, cam.combined);
            cam.setToOrtho(false, game.getWidth(), game.getHeight());
        }
        for (int i = 0; i < blocks.size; i++) {
            blocks.get(i).render(sb);
        }
    }
    private void loadContent(){
        resources.loadTexture("res/images/menu2.png");
        resources.loadTexture("res/images/bgs.png");
        resources.loadTexture("res/images/hud2.png");
        resources.loadTexture("res/images/bunny.png");
        resources.loadTexture("res/images/crystal.png");
        resources.loadTexture("res/images/blocks2.png");
        //resources.loadTexture("res/images/spikes.png");
        resources.loadTexture("res/images/Play.png");
        resources.loadTexture("res/images/Leader.png");
        resources.loadTexture("res/images/Track.png");
        resources.loadTexture("res/images/PlatformerPack/Player/p1_walk/fix.png");

        resources.loadSound("res/sfx/jump.wav");
        resources.loadSound("res/sfx/crystal.wav");
        resources.loadSound("res/sfx/levelselect.wav");
        resources.loadSound("res/sfx/hit.wav");
        resources.loadSound("res/sfx/changeblock.wav");

        resources.loadFile("res/music/test.mp3");
    }
    public void dispose(){

    }
}
