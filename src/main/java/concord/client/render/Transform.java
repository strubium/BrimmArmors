package concord.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.item.ItemDisplayContext;


import java.util.Arrays;
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

    public void apply(ItemDisplayContext type, PoseStack poseStack) {
        switch (type) {
            case GUI -> GUI.setup(poseStack);
            case GROUND -> DROP.setup(poseStack);
            case FIRST_PERSON_RIGHT_HAND -> RIGHT_HAND.setup(poseStack);
            case FIRST_PERSON_LEFT_HAND -> LEFT_HAND.setup(poseStack);
            case THIRD_PERSON_LEFT_HAND -> LEFT_HAND_3D.setup(poseStack);
            case THIRD_PERSON_RIGHT_HAND -> RIGHT_HAND_3D.setup(poseStack);
            default -> {
                // no-op
            }
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

        public void setup(PoseStack poseStack) {
            poseStack.mulPose(Vector3f.XP.rotationDegrees(rotate[0]));
            poseStack.mulPose(Vector3f.YP.rotationDegrees(rotate[1]));
            poseStack.mulPose(Vector3f.ZP.rotationDegrees(rotate[2]));
            poseStack.scale(scale[0], scale[1], scale[2]);
            poseStack.translate(translate[0], translate[1], translate[2]);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Matrix matrix = (Matrix) o;
            return Arrays.equals(translate, matrix.translate)
                    && Arrays.equals(rotate, matrix.rotate)
                    && Arrays.equals(scale, matrix.scale);
        }

        @Override
        public int hashCode() {
            int result = Arrays.hashCode(translate);
            result = 31 * result + Arrays.hashCode(rotate);
            result = 31 * result + Arrays.hashCode(scale);
            return result;
        }
    }
}
