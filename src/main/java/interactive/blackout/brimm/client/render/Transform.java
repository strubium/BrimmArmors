package interactive.blackout.brimm.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.util.math.vector.Vector3f;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Consumer;

public class Transform {

    public Matrix ARMOR = new Matrix().identify();

    public Matrix WORKBENCH = new Matrix().identify();
    public Matrix GUI = new Matrix().identify();
    public Matrix RIGHT_HAND_3D = new Matrix().identify();
    public Matrix RIGHT_HAND = new Matrix().identify();
    public Matrix LEFT_HAND_3D = new Matrix().identify();
    public Matrix LEFT_HAND = new Matrix().identify();
    public Matrix DROP = new Matrix().identify();

    protected Transform() {

    }

    public static Transform create() {
        return new Transform();
    }

    public Transform accept(Consumer<Transform> consumer) {
        consumer.accept(this);
        return this;
    }

    public void apply(ItemCameraTransforms.TransformType type, MatrixStack matrix) {
        switch (type) {
            case GUI:
                GUI.setup(matrix);
                break;
            case GROUND:
                DROP.setup(matrix);
                break;
            case FIRST_PERSON_RIGHT_HAND:
                RIGHT_HAND.setup(matrix);
                break;
            case FIRST_PERSON_LEFT_HAND:
                LEFT_HAND.setup(matrix);
                break;
            case THIRD_PERSON_LEFT_HAND:
                LEFT_HAND_3D.setup(matrix);
                break;
            case THIRD_PERSON_RIGHT_HAND:
                RIGHT_HAND_3D.setup(matrix);
                break;
            default:
                break;
        }
    }

    public static class Matrix {
        public static final Matrix IDENTITY = new Matrix().identify();
        public float[] translate;
        public float[] rotate;
        public float[] scale;

        public Matrix identify() {
            this.translate = new float[]{0, 0, 0};
            this.rotate = new float[]{0, 0, 0};
            this.scale = new float[]{1, 1, 1};
            return this;
        }

        public Matrix setTranslate(float x, float y, float z) {
            this.translate = new float[]{x, y, z};
            return this;
        }

        public Matrix setRotate(float x, float y, float z) {
            this.rotate = new float[]{x, y, z};
            return this;
        }

        public Matrix setScale(float x, float y, float z) {
            this.scale = new float[]{x, y, z};
            return this;
        }

        public void setup(MatrixStack matrix) {
            matrix.mulPose(Vector3f.XP.rotationDegrees(rotate[0]));
            matrix.mulPose(Vector3f.YP.rotationDegrees(rotate[1]));
            matrix.mulPose(Vector3f.ZP.rotationDegrees(rotate[2]));
            matrix.scale(scale[0], scale[1], scale[2]);
            matrix.translate(translate[0], translate[1], translate[2]);
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            Matrix matrix = (Matrix) o;
            return Objects.deepEquals(translate, matrix.translate) && Objects.deepEquals(rotate, matrix.rotate) && Objects.deepEquals(scale, matrix.scale);
        }

        @Override
        public int hashCode() {
            return Objects.hash(Arrays.hashCode(translate), Arrays.hashCode(rotate), Arrays.hashCode(scale));
        }
    }

}
