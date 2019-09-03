package com.branwilliams.bundi.voxel.system.player;

import com.branwilliams.bundi.engine.core.Engine;
import com.branwilliams.bundi.engine.core.Lockable;
import com.branwilliams.bundi.engine.core.Window;
import com.branwilliams.bundi.engine.ecs.AbstractSystem;
import com.branwilliams.bundi.engine.ecs.EntitySystemManager;
import com.branwilliams.bundi.engine.ecs.matchers.ClassComponentMatcher;
import com.branwilliams.bundi.voxel.components.PlayerState;
import com.branwilliams.bundi.voxel.VoxelScene;
import com.branwilliams.bundi.voxel.voxels.Voxels;
import org.joml.Vector3f;

/**
 * @author Brandon
 * @since August 10, 2019
 */
public class PlayerInteractSystem extends AbstractSystem implements Window.MouseListener {

    /** This is the number of updates that this system will wait for while the mouse button is held down. */
    private static final int TICKS_PER_INTERACTION = 15;

    private static final int MOUSE_LEFT_CLICK = 0;

    private static final int MOUSE_RIGHT_CLICK = 1;

    private static final int NO_MOUSE = -1;

    private final VoxelScene scene;

    private final Lockable lockable;

    private float interactionDelay;

    private int interactionState = NO_MOUSE;

    public PlayerInteractSystem(VoxelScene scene) {
        this(scene, Lockable.unlocked());
    }

    public PlayerInteractSystem(VoxelScene scene, Lockable lockable) {
        super(new ClassComponentMatcher(PlayerState.class));
        this.scene = scene;
        this.scene.addMouseListener(this);
        this.lockable = lockable;
    }

    @Override
    public void init(Engine engine, EntitySystemManager entitySystemManager, Window window) {

    }

    @Override
    public void update(Engine engine, EntitySystemManager entitySystemManager, double deltaTime) {

    }

    @Override
    public void fixedUpdate(Engine engine, EntitySystemManager entitySystemManager, double deltaTime) {
        if (lockable.isLocked()) {
            return;
        }

        if (interactionDelay > 0) {
            interactionDelay--;
        } else if (interactionState != NO_MOUSE) {
            pressMouseButton(scene.getPlayerState(),
                    engine.getWindow().getMouseX(), engine.getWindow().getMouseY(),
                    interactionState);
        }
    }

    @Override
    public void move(Window window, float newMouseX, float newMouseY, float oldMouseX, float oldMouseY) {

    }

    @Override
    public void press(Window window, float mouseX, float mouseY, int buttonId) {
        if (lockable.isLocked()) {
            return;
        }

        interactionState = buttonId;
        pressMouseButton(scene.getPlayerState(), mouseX, mouseY, buttonId);
    }

    @Override
    public void release(Window window, float mouseX, float mouseY, int buttonId) {
        interactionState = NO_MOUSE;
    }

    @Override
    public void wheel(Window window, double xoffset, double yoffset) {
    }

    private void pressMouseButton(PlayerState playerState, float mouseX, float mouseY, int buttonId) {
        if (playerState.getRaycast() != null) {
            switch (buttonId) {
                case MOUSE_LEFT_CLICK:
                    scene.getVoxelWorld().getChunks().setVoxelAtPosition(Voxels.air, playerState.getRaycast().blockPosition);
                    interactionDelay = TICKS_PER_INTERACTION;
                    break;
                case MOUSE_RIGHT_CLICK:
                    Vector3f placePosition = new Vector3f(playerState.getRaycast().blockPosition);
                    placePosition.add(playerState.getRaycast().face);
                    if (scene.getVoxelWorld().getChunks().getVoxelAtPosition(placePosition).isAir()) {
                        scene.getVoxelWorld().getChunks().setVoxelAtPosition(playerState.getVoxelInHand(), placePosition);
                        interactionDelay = TICKS_PER_INTERACTION;
                    }
                    break;
                case NO_MOUSE:
                    break;
                default:
                    break;
            }
        }
    }
}