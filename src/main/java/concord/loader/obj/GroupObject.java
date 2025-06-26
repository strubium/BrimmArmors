package concord.loader.obj;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import java.util.ArrayList;

public class GroupObject implements Comparable<GroupObject> {
    public String name;
    public ArrayList<Face> faces;

    public GroupObject() {
        this("");
    }

    public GroupObject(String name) {
        this.faces = new ArrayList<>();
        this.name = name;
    }

    public void render(PoseStack poseStack, VertexConsumer vertexConsumer, int combinedLight, int combinedOverlay) {
        for (Face face : this.faces) {
            face.addFaceForRender(poseStack, vertexConsumer, combinedLight, combinedOverlay);
        }
    }

    @Override
    public int compareTo(GroupObject other) {
        return this.name.compareTo(other.name);
    }
}
