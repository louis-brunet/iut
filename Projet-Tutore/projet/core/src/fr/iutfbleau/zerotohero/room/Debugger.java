package fr.iutfbleau.zerotohero.room;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import fr.iutfbleau.zerotohero.utils.Coordinates;
import fr.iutfbleau.zerotohero.actors.MovingPlatform;
import fr.iutfbleau.zerotohero.physics.AxisAlignedBoundingBox;
import fr.iutfbleau.zerotohero.physics.Body;

import java.util.List;

public class Debugger {
    private static ShapeRenderer shapeRenderer = new ShapeRenderer();

    /**
     * Renders the Collisions of the given map on the given camera
     * @param map The map to render the collisions of
     * @param camera The camera on which the collisions will be rendered
     */
    public static void renderCollisions(Room map, Camera camera) {
        List<Rectangle> solidCollisionRectangles = map.getSolidCollisionList();
        List<Rectangle> jumpThroughCollisionRectangles = map.getJumpThroughCollisionList();
        List<Rectangle> damageCollisionRectangles = map.getDamageCollisionList();

        Gdx.gl.glEnable(GL20.GL_BLEND);
        shapeRenderer.setProjectionMatrix(camera.combined);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(new Color(1f,0f,0f,0.25f));
        for (Rectangle rectangle : solidCollisionRectangles) {
            shapeRenderer.rect(rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight());
        }

        shapeRenderer.setColor(new Color(0f,1f,0f,0.25f));
        for (Rectangle rectangle : jumpThroughCollisionRectangles) {
            shapeRenderer.rect(rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight());
        }

        shapeRenderer.setColor(new Color(0f,0f,1f,0.25f));
        for (Rectangle rectangle : damageCollisionRectangles) {
            shapeRenderer.rect(rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight());
        }

        shapeRenderer.end();
//        shapeRenderer.setColor(new Color(0f,0f,1f,0.25f));
        map.getWorld().getBodies().forEach(
                body -> Debugger.renderBoundingShape(body, camera));

//        map.getAllActors().forEach((actor) -> {
//            if (actor instanceof PhysicsActor) {
//                PhysicsActor physicsActor = (PhysicsActor) actor;
//                Body body = physicsActor.getBody();
//                AxisAlignedBoundingBox boundingBox = (AxisAlignedBoundingBox) body.getBounds();
//                shapeRenderer.end();
//                boundingBox.drawDebug(shapeRenderer, new Color(1f, 0.5f, 0f, 0.5f));
//                shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
//            }
//            else
//                shapeRenderer.rect(actor.getX(), actor.getY(), actor.getWidth(), actor.getHeight());
//        });

        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    /**
     * Renders the Spawn Points of the given map on the given camera
     * @param map The map to render the Spawn Points of
     * @param camera The camera on which the Spawn Points will be rendered
     */
    public static void renderSpawnPoints(Room map, Camera camera) {
        int arrowSize = 16;

        Coordinates playerSpawn = map.getPlayerSpawnPoint();
        List<Coordinates> enemySpawnPoints = map.getEnemySpawnPointList();

        shapeRenderer.setProjectionMatrix(camera.combined);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        Gdx.gl.glLineWidth(arrowSize/8);

        shapeRenderer.setColor(new Color(0f,1f,1f,1f));
        shapeRenderer.triangle(playerSpawn.getX(), playerSpawn.getY(), playerSpawn.getX()-arrowSize, playerSpawn.getY()+arrowSize, playerSpawn.getX()+arrowSize, playerSpawn.getY()+arrowSize);

        shapeRenderer.setColor(new Color(1f,1f,0f,1f));
        for (Coordinates enemySpawn : enemySpawnPoints) {
            shapeRenderer.triangle(enemySpawn.getX(), enemySpawn.getY(), enemySpawn.getX()-arrowSize, enemySpawn.getY()+arrowSize, enemySpawn.getX()+arrowSize, enemySpawn.getY()+arrowSize);
        }

        shapeRenderer.end();
    }

    public static void renderActorPositions(Room map, Camera camera) {
        int arrowSize = 16;

        shapeRenderer.setProjectionMatrix(camera.combined);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        Gdx.gl.glLineWidth(arrowSize/8);


        for(Actor actor: map.getAllActors()) {
            if (actor instanceof MovingPlatform) {
                shapeRenderer.setColor(new Color(0.4f,0f,0.4f,1f));
                for (Coordinates point : ((MovingPlatform) actor).getPath()) {
                    shapeRenderer.triangle(point.getX(), point.getY(), point.getX() - arrowSize, point.getY() + arrowSize, point.getX() + arrowSize, point.getY() + arrowSize);
                }
            }
            shapeRenderer.setColor(new Color(1f,0f,1f,1f));
            shapeRenderer.triangle(actor.getX(), actor.getY(), actor.getX()-arrowSize,actor.getY()+arrowSize, actor.getX()+arrowSize, actor.getY()+arrowSize);
        }

        shapeRenderer.end();
    }

//    public static void renderActorBounds(TestMap map, Camera cam) {
//        map.getWorld().getBodies().forEach(
//                body -> TestDebugger.renderBoundingShape(body, cam) );
//    }

    public static void renderBoundingShape(Body body, Camera camera) {
        shapeRenderer.setProjectionMatrix(camera.combined);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
//        Gdx.gl.glLineWidth(2);

        shapeRenderer.setColor(new Color(1f,0f,0f,0.7f));

        switch(body.getBounds().getType()) {
            case AABB:
                AxisAlignedBoundingBox aabb = (AxisAlignedBoundingBox) body.getBounds();
                shapeRenderer.rect(aabb.getCenter().getX() - aabb.getHalfWidth(),
                        aabb.getCenter().getY() - aabb.getHalfHeight(),
                        aabb.getHalfWidth() * 2,
                        aabb.getHalfHeight() * 2);
                break;
            case CIRCLE:
                shapeRenderer.circle(body.getBounds().getCenter().getX(),
                        body.getBounds().getCenter().getY(),
                        body.getBounds().getHalfWidth());
                break;
            default:
                throw new IllegalArgumentException("BoundingShape type not recognized.");
        }

        shapeRenderer.end();
    }

    public static void dispose() {
        Debugger.shapeRenderer.dispose();
        Debugger.shapeRenderer = null;
    }
}
