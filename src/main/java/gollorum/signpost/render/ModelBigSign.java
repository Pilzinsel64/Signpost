package gollorum.signpost.render;

import net.minecraft.client.model.ModelBase;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gollorum.signpost.blocks.tiles.BigPostPostTile;
import gollorum.signpost.util.BigBaseInfo;

@SideOnly(Side.CLIENT)
public class ModelBigSign extends ModelBase {

    public BigBoard board;

    public ModelBigSign(boolean is16) {

        if (is16) {
            textureWidth = 16;
            textureHeight = 16;
        } else {
            textureWidth = 16;
            textureHeight = 32;
        }

        board = new BigBoard(this);

    }

    public void render(BigPostRenderer postRenderer, float f1, float f5, BigBaseInfo tilebases, BigPostPostTile tile,
        double rotation, boolean canSeeSignBoard) {
        super.render(null, 0, f1, 0, 0, 0, f5);
        if (tile.isItem || canSeeSignBoard) {
            GL11.glPushMatrix();
            GL11.glRotated(180, 0, 0, 1);
            GL11.glTranslated(0, -1, 0);
            GL11.glRotated(-Math.toDegrees(rotation), 0, 1, 0);
            board.render(f5, tile.isItem ? false : tilebases.sign.flip);
            GL11.glPopMatrix();
        }
    }

    public void renderOverlay(BigBaseInfo tilebases, float f5, double rotation) {
        GL11.glPushMatrix();
        GL11.glRotated(Math.toDegrees(rotation), 0, 1, 0);
        GL11.glTranslated(0, 0.25, 2.5 / 16.0);
        GL11.glScaled(1.01, 1.01, 1.1);
        GL11.glTranslated(0, -0.75, -2.5 / 16.0);
        GL11.glRotated(180, 0, 0, 1);
        GL11.glTranslated(0, -1.5, 0);
        board.render(f5, tilebases.sign.flip);
        GL11.glPopMatrix();
    }

}
