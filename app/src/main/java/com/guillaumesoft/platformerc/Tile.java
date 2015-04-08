package com.guillaumesoft.platformerc;

import com.badlogic.androidgames.framework.DynamicGameObject;
import com.badlogic.androidgames.framework.gl.TextureRegion;
import com.badlogic.androidgames.framework.math.Vector2;

enum TileCollision
{
    Passable(0), Impassable(1), Platform(2), Ladder(3), Checkpoint(4);

    final int numTab;

    TileCollision(int num)
    {
        this.numTab = num;
    }

    public int getValue()
    {
        return this.numTab;
    }
}

/// <summary>
/// Stores the appearance and collision behavior of a tile.
/// </summary>
class Tile extends DynamicGameObject
{
    ////////////////////////////////////////
    // PUBLIC CLASS VARAIBLES
    public TextureRegion texture;
    public TileCollision Collision;

    public static final float TILE_WIDTH  = 42;
    public static final float TILE_HEIGHT = 32;
    public static final Vector2 Size = new Vector2(TILE_WIDTH, TILE_HEIGHT);
    public static final float Center = TILE_HEIGHT /2;

    /////////////////////////////////////////
    /// Constructs a new tile.
    /// </summary>
    public Tile(float x, float y, TextureRegion texture, TileCollision collision)
    {
        super(x, y, TILE_WIDTH, TILE_HEIGHT);

        this.texture = texture;
        Collision    = collision;
    }
}
