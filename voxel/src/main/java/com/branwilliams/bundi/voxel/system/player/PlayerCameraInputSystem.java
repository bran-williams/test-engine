package com.branwilliams.bundi.voxel.system.player;

import com.branwilliams.bundi.engine.core.Engine;
import com.branwilliams.bundi.engine.ecs.EntitySystemManager;
import com.branwilliams.bundi.engine.ecs.IEntity;
import com.branwilliams.bundi.engine.ecs.matchers.ClassComponentMatcher;
import com.branwilliams.bundi.engine.shader.Camera;
import com.branwilliams.bundi.engine.shader.Transformable;
import com.branwilliams.bundi.engine.systems.MouseControlSystem;
import com.branwilliams.bundi.engine.util.Mathf;
import com.branwilliams.bundi.voxel.VoxelScene;
import com.branwilliams.bundi.voxel.components.CameraComponent;
import com.branwilliams.bundi.voxel.components.PlayerState;

/**
 * Updates the camera rotation and position in order to follow an entities transformable and the mouse movements.
 *
 * @author Brandon
 * @since August 16, 2019
 */
public class PlayerCameraInputSystem extends MouseControlSystem {

    public PlayerCameraInputSystem(VoxelScene scene) {
        super(scene, scene, new ClassComponentMatcher(Transformable.class, PlayerState.class, CameraComponent.class));
    }

    @Override
    protected void mouseMove(Engine engine, EntitySystemManager entitySystemManager, double interval, float mouseX, float mouseY, float oldMouseX, float oldMouseY) {
        for (IEntity entity : entitySystemManager.getEntities(this)) {
            CameraComponent cameraComponent = entity.getComponent(CameraComponent.class);

            Camera camera = cameraComponent.getCamera();
            float dYaw = (mouseX - oldMouseX) * cameraComponent.getCameraSpeed();
            float dPitch = -(mouseY - oldMouseY) * cameraComponent.getCameraSpeed();
            camera.rotate(dYaw, dPitch);
        }
    }
}
