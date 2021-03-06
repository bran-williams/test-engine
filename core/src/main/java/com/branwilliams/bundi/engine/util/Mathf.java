package com.branwilliams.bundi.engine.util;

import com.branwilliams.bundi.engine.shader.Camera;
import com.branwilliams.bundi.engine.shader.Projection;
import com.branwilliams.bundi.engine.shader.Transformable;
import com.branwilliams.bundi.engine.core.window.Window;
import java.util.Random;
import org.joml.*;

import java.lang.Math;

/**
 * Math utility class.
 * Many functions stolen from libgdx on 10/13/2017.
 */
public enum Mathf {
    INSTANCE;

    private static final int BIG_ENOUGH_INT = 16 * 1024;
    private static final double BIG_ENOUGH_FLOOR = BIG_ENOUGH_INT;

    private static final double CEIL = 0.9999999;
    private static final double BIG_ENOUGH_CEIL = 16384.999999999996;
    private static final double BIG_ENOUGH_ROUND = BIG_ENOUGH_INT + 0.5f;

    private static final int SIN_BITS = 14; // 16KB. Adjust for accuracy.
    private static final int SIN_MASK = ~(-1 << SIN_BITS);
    private static final int SIN_COUNT = SIN_MASK + 1;

    public static final float PI = 3.1415927f;

    private static final float radFull = PI * 2;
    private static final float degFull = 360;

    private static final float radToIndex = SIN_COUNT / radFull;
    private static final float degToIndex = SIN_COUNT / degFull;
    public static final float degreesToRadians = PI / 180;

    /**
     * Example usage
     * <pre>
     * getTwosPower(-1) = -1
     * getTwosPower(0) = -1
     * getTwosPower(1) = 0
     * getTwosPower(2) = 1
     * getTwosPower(4) = 2
     * getTwosPower(8) = 3
     * ... etc
     * </pre>
     * @return -1 if the value is equal to or below zero, otherwise the minimum two's power that can encompass the input
     * value.
     * */
    public static int getTwosPower(float n) {
        int val = (int) n;

        if (val <= 0)
            return -1;

        int pow = -1;
        while (val != 0) {
            val = val / 2;
            pow++;
        }

        return pow;
    }

    public static boolean isPowerOfTwo(int n) {
        return n > 0 && (n & (n - 1)) == 0;
    }

    public static float smoothStep(float edge0, float edge1, float x) {
        float t = clamp((x - edge0) / (edge1 - edge0), 0.0F, 1.0F);
        return t * t * (3.0F - 2.0F * t);
    }

    public static Vector2f smoothStep(Vector2f edge0, Vector2f edge1, float x) {
        return new Vector2f(smoothStep(edge0.x, edge1.x, x), smoothStep(edge0.y, edge1.y, x));
    }

        public static int floor(double val) {
        return (int)(val + 1024.0D) - 1024;
    }

    public static float wrapDegrees(float value) {
        value = value % 360.0F;
        if (value >= 180.0F) value -= 360.0F;
        if (value < -180.0F) value += 360.0F;
        return value;
    }

    public static float wrapDegrees(double value) {
        return wrapDegrees((float)value);
    }

    public static int ceil(float value) {
        return BIG_ENOUGH_INT - (int)(BIG_ENOUGH_FLOOR - value);
    }

    public static int ceil(double value) {
        return BIG_ENOUGH_INT - (int)(BIG_ENOUGH_FLOOR - value);
    }

    public static float sqrt(float value) {
        return (float)Math.sqrt((double)value);
    }

    public static float sqrt(double value) {
        return (float)Math.sqrt(value);
    }

    /**
     * Linearly interpolates between prev and cur based on the delta value.
     *
     * */
    public static double interpolate(double prev, double cur, double delta) {
        return prev + ((cur - prev) * delta);
    }

    /**
     * Linearly interpolates between prev and cur based on the delta value.
     *
     * */
    public static float interpolate(float prev, float cur, float delta) {
        return prev + ((cur - prev) * delta);
    }

    /**
     * Consine interpolation between prev and cur based on the delta value.
     * */
    public static double interpolateCosine(double prev, double cur, double delta) {
        double theta = delta * PI;
        float f = (float) (1F - Math.cos(theta)) * 0.5F;
        return prev * (1F - f) + cur * f;
    }

    /**
     * Converts an angle from degrees to radians.
     * */
    public static float toRadians(float angdeg) {
        return angdeg * degreesToRadians;
    }

    public static float sin(float radians) {
        return Sin.table[(int)(radians * radToIndex) & SIN_MASK];
    }

    public static float sin(double radians) {
        return Sin.table[(int)(radians * radToIndex) & SIN_MASK];
    }

    public static float cos(float radians) {
        return Sin.table[(int)((radians + PI / 2) * radToIndex) & SIN_MASK];
    }

    public static float cos(double radians) {
        return Sin.table[(int)((radians + PI / 2) * radToIndex) & SIN_MASK];
    }

    /**
     * Clamps the given input between the maximum and -maximum
     */
    public static float clamp(float input, float max) {
        return clamp(input, -max, max);
    }

    /**
     * Clamps the input between the maximum and minimum values.
     * */
    public static float clamp(float input, float min, float max) {
        return Math.max(Math.min(input, max), min);
    }

    /**
     * Clamps the given input between the maximum and -maximum
     */
    public static double clamp(double input, double max) {
        return clamp(input, -max, max);
    }

    /**
     * Clamps the input between the maximum and minimum values.
     * */
    public static double clamp(double input, double min, double max) {
        return Math.max(Math.min(input, max), min);
    }

    /**
     * Clamps the given input between the maximum and -maximum
     */
    public static double clamp(int input, int max) {
        return clamp(input, -max, max);
    }

    /**
     * Clamps the input between the maximum and minimum values.
     * */
    public static int clamp(int input, int min, int max) {
        return Math.max(Math.min(input, max), min);
    }

    /**
     * Linearly interpolates from x to y by the amount.
     * This produces a new vector.
     * */
    public static Vector3f lerp(Vector3f x, Vector3f y, float amount) {
        return new Vector3f(lerp(x.x, y.x, amount), lerp(x.y, y.y, amount), lerp(x.z, y.z, amount));
    }

    /**
     * Linearly interpolates from x to y by the amount.
     * This produces a new vector.
     * */
    public static Vector2f lerp(Vector2f x, Vector2f y, float amount) {
        return new Vector2f(lerp(x.x, y.x, amount), lerp(x.y, y.y, amount));
    }

    /**
     * Linearly interpolates from x to y by the amount.
     * */
    public static float lerp(float x, float y, float amount) {
        return x * (1F - amount) + y * amount;
    }

    public static int abs(int input) {
        return input >= 0 ? input : -input;
    }

    public static float abs(float input) {
        return input >= 0 ? input : -input;
    }

    public static double abs(double input) {
        return input >= 0 ? input : -input;
    }

    /**
     * @TODO Find a faster formula?
     *
     * @return The base n log of the given number.
     * */
    public static float log(int base, float number) {
        return (float) (Math.log(number) / Math.log(base));
    }

    /**
     * @see
     * */
    public static float barryCentric(Vector3f p1, Vector3f p2, Vector3f p3, Vector2f pos) {
        float det = (p2.z - p3.z) * (p1.x - p3.x) + (p3.x - p2.x) * (p1.z - p3.z);
        float l1 = ((p2.z - p3.z) * (pos.x - p3.x) + (p3.x - p2.x) * (pos.y - p3.z)) / det;
        float l2 = ((p3.z - p1.z) * (pos.x - p3.x) + (p1.x - p3.x) * (pos.y - p3.z)) / det;
        float l3 = 1.0f - l1 - l2;
        return l1 * p1.y + l2 * p2.y + l3 * p3.y;
    }

    public static int average(int... numbers) {
        if (numbers == null || numbers.length == 0) {
            return 0;
        }

        int sum = 0;
        for (int number : numbers) {
            sum += number;
        }
        return sum / numbers.length;
    }

    public static float average(float... numbers) {
        if (numbers == null || numbers.length == 0) {
            return 0;
        }

        float sum = 0;
        for (float number : numbers) {
            sum += number;
        }
        return sum / (float) numbers.length;
    }

    public static double average(double... numbers) {
        if (numbers == null || numbers.length == 0) {
            return 0;
        }

        double sum = 0;
        for (double number : numbers) {
            sum += number;
        }
        return sum / (double) numbers.length;
    }

    private static final Matrix4f modelMatrix = new Matrix4f(), modelViewMatrix = new Matrix4f(), viewMatrix = new Matrix4f();

    /**
     * @return A projection matrix with the given fov, znear, and zfar values.
     * */
    public static Matrix4f createPerspectiveMatrix(Window window, Matrix4f matrix, float fov, float znear, float zfar) {
        // Calculate the aspect ratio
        float aspectRatio = (float) window.getWidth() / window.getHeight();

        // Set the projection matrix using our fov, aspect ratio, znear, and zfar values.
        return matrix.setPerspective(toRadians(fov), aspectRatio, znear, zfar);
    }

    /**
     * @return A projection matrix with the given fov, znear, and zfar values.
     * */
    public static Matrix4f createPerspectiveMatrix(Window window, float fov, float znear, float zfar) {
        return createPerspectiveMatrix(window, new Matrix4f(), fov, znear, zfar);
    }

    /**
     * @return A frustum matrix with the given left, right, top, bottom, znear, and zfar values.
     * */
    public static Matrix4f createFrustumMatrix(Window window, Matrix4f matrix, float left, float right, float bottom, float top, float znear, float zfar) {
        return matrix.setFrustum(left, right, bottom, top, znear, zfar);
    }

    /**
     * Assumes the left to be 0, the right to be the width of the window, the top to be 0, and the bottom to be the height of the window. <br/>
     * @return A frustum matrix with the given znear, and zfar values.
     * */
    public static Matrix4f createFrustumMatrix(Window window, float znear, float zfar) {
        return createFrustumMatrix(window, new Matrix4f(), 0, window.getWidth(), window.getHeight(), 0, znear, zfar);
    }

    /**
     * Creates an orthographic projection matrix from the window information given.
     * */
    public static Matrix4f createOrtho2dMatrix(Matrix4f matrix, Vector2f size) {
        return matrix.setOrtho2D(0, size.x, size.y, 0);
    }

    /**
     * Creates a projection matrix from the window information given.
     * */
    public static Matrix4f createOrtho2dMatrix(Vector2f size) {
        return createOrtho2dMatrix(new Matrix4f(), size);
    }

    /**
     * Creates an orthographic projection matrix from the window information given.
     * */
    public static Matrix4f createOrtho2dMatrix(Matrix4f matrix, Window window) {
        return matrix.setOrtho2D(0, window.getWidth(), window.getHeight(), 0);
    }

    /**
     * Creates a projection matrix from the window information given.
     * */
    public static Matrix4f createOrtho2dMatrix(Window window) {
        return createOrtho2dMatrix(new Matrix4f(), window);
    }

    /**
     * Creates a model matrix given the transformable which always faces the camera's position.
     * */
    public static Matrix4f toSphericalBillboardedModelMatrix(Matrix4f matrix, Transformable transformable, Camera camera) {
        return matrix.identity().translate(transformable.getPosition()).
                rotateY(toRadians(180 - camera.getPitch())).
                rotateX(toRadians(transformable.getRotationAsEuler().x)).
                rotateZ(toRadians(transformable.getRotationAsEuler().z)).
                scale(transformable.getScale());
    }

    public static Matrix4f toSphericalBillboardedModelMatrix(Transformable transformable, Camera camera) {
        return toSphericalBillboardedModelMatrix(modelMatrix, transformable, camera);
    }

    /**
     * Creates a model matrix given the transformable which always faces the camera's position in yaw.
     * */
    public static Matrix4f toCylindricalBillboardedModelMatrix(Matrix4f matrix, Transformable transformable, Camera camera) {
        return matrix.identity().translate(transformable.getPosition()).
                rotateY(toRadians(180 - camera.getPitch())).
                rotateX(toRadians(camera.getYaw())).
                rotateZ(toRadians(transformable.getRotationAsEuler().z)).
                scale(transformable.getScale());
    }

    public static Matrix4f toCylindricalBillboardedModelMatrix(Transformable transformable, Camera camera) {
        return toCylindricalBillboardedModelMatrix(modelMatrix, transformable, camera);
    }

    /**
     * This will create a matrix representation of a given transformable. These matrices are sometimes called
     * transformation matrices, world matrices, model matrices.
     * @return A matrix representation of the provided transformable.
     * */
    public static Matrix4f toModelMatrix(Matrix4f matrix, Transformable transformable) {
        return matrix.identity()
                .translate(transformable.getPosition())
                .rotate(transformable.getRotation())
                //.rotateX(toRadians(transformable.getRotationAsEuler().x))
                //.rotateY(toRadians(transformable.getRotationAsEuler().y))
                //.rotateZ(toRadians(transformable.getRotationAsEuler().z))
                .scale(transformable.getScale());
    }

    /**
     * This will create a matrix representation of a given transformable. These matrices are sometimes called
     * transformation matrices, world matrices, model matrices.
     * @return A matrix representation of the provided transformable.
     * */
    public static Matrix4f toModelMatrix(Transformable transformable) {
        return toModelMatrix(modelMatrix, transformable);
    }

    /**
     * Produces a view matrix from a provided camera.
     * */
    public static Matrix4f toViewMatrix(Camera camera) {
        return toViewMatrix(viewMatrix, camera);
    }

    /**
     * Produces a view matrix from a provided camera.
     * */
    public static Matrix4f toViewMatrix(Matrix4f matrix, Camera camera) {
        return matrix.identity()
                .rotateX(toRadians(camera.getYaw()))
                .rotateY(toRadians(camera.getPitch()))
                .translate(-camera.getPosition().x, -camera.getPosition().y, -camera.getPosition().z);
    }

    /**
     * Produces a modelview matrix from the provided transformation and view matrix.
     * */
    public static Matrix4f toModelViewMatrix(Transformable transformable, Matrix4f viewMatrix, Matrix4f destination) {
        return viewMatrix.mul(toModelMatrix(transformable), destination);
    }

    /**
     * Produces a modelview matrix from the provided transformation and view matrix.
     * */
    public static Matrix4f toModelViewMatrix(Transformable transformable, Matrix4f viewMatrix) {
        return toModelViewMatrix(transformable, viewMatrix, modelViewMatrix);
    }

    public static Vector3f unprojectMouseCoordinates(Projection projection, Camera camera, Window window) {
        return unproject(projection.toProjectionMatrix(), camera.toViewMatrix(),
                window.getWidth(), window.getHeight(),
                window.getMouseX(), window.getMouseY());
    }

    /**
     *
     * */
    public static Vector3f unproject(Matrix4f projectionMatrix, Matrix4f viewMatrix,
                                     int windowWidth, int windowHeight,
                                     float windowX, float windowY) {
        // convert to ndc.
        float x = (2F * windowX) / windowWidth - 1F;
        float y = 1F - (2F * windowY) / windowHeight;

        // unproject this into eye coordinates
        Vector4f rayClip = new Vector4f(x, y, -1F, 1F);
        projectionMatrix.invert(new Matrix4f()).transform(rayClip);

        // unproject eye coordinates into world coordinates
        Vector4f rayEye = new Vector4f(rayClip.x, rayClip.y, -1F, 0F);
        viewMatrix.invert(new Matrix4f()).transform(rayEye);

        // normalize the world coordinates and ta-da!
        Vector3f mouseRay = new Vector3f(rayEye.x, rayEye.y, rayEye.z);
        mouseRay.normalize();
        return mouseRay;
    }

    public static Vector3f getRandomPointWithinSphere(Random random) {
        return getRandomPointWithinSphere(random, new Vector3f());
    }

    public static Vector3f getRandomPointWithinSphere(Random random, Vector3f vector) {
        float x = (float) random.nextGaussian();
        float y = (float) random.nextGaussian();
        float z = (float) random.nextGaussian();

        return vector.set(x, y, z).normalize();
    }

    /**
     * Sin table used for fast trigonometry functions.
     * */
    private static class Sin {

        static final float[] table = new float[SIN_COUNT];

        static {
            for (int i = 0; i < SIN_COUNT; i++)
                table[i] = (float)Math.sin((i + 0.5f) / SIN_COUNT * radFull);

            for (int i = 0; i < 360; i += 90)
                table[(int)(i * degToIndex) & SIN_MASK] = (float)Math.sin(i * degreesToRadians);
        }
    }

}
