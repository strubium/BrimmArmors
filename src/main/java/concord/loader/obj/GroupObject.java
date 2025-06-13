package concord.loader.obj;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL32;

import java.util.ArrayList;

public class GroupObject implements Comparable<GroupObject> {
    private final int glListId;
    public String name;
    public ArrayList<Face> faces;
    public int glDrawingMode;
    private boolean first;

    public GroupObject() {
        this("");
    }

    public GroupObject(String name) {
        this(name, -1);
    }

    public GroupObject(String name, int glDrawingMode) {
        this.faces = new ArrayList<>();
        this.name = name;
        this.glDrawingMode = glDrawingMode;
        this.glListId = GL32.glGenLists(1);
        this.first = true;
    }

    @OnlyIn(Dist.CLIENT)
    public void render() {
        if (!this.first) {
            GL32.glCallList(this.glListId);
        } else {
            this.first = false;
            GL32.glNewList(this.glListId, 4865);
            if (!this.faces.isEmpty()) {
                Tessellator tessellator = Tessellator.getInstance();
                BufferBuilder builder = tessellator.getBuilder();
                builder.begin(this.glDrawingMode, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
                this.render(tessellator);
                tessellator.end();
            }

            GL32.glEndList();
        }

    }

    @OnlyIn(Dist.CLIENT)
    public void render(Tessellator tessellator) {
        for (Face face : this.faces)
            face.addFaceForRender(tessellator);
    }

    public void render(BufferBuilder builder, MatrixStack matrix) {
        for (Face face : this.faces)
            face.addFaceForRender(builder, matrix);
    }

    @OnlyIn(Dist.CLIENT)
    public void render(MatrixStack matrix, IVertexBuilder buffer, int combinedLight, int combinedOverlay) {
        for (Face face : this.faces)
            face.addFaceForRender(matrix, buffer, combinedLight, combinedOverlay);
    }

    @Override
    public int compareTo(GroupObject other) {
        return this.name.compareTo(other.name);
    }
}
