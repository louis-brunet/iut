package fr.iutfbleau.zerotohero.actors.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import fr.iutfbleau.zerotohero.ZeroToHero;
import fr.iutfbleau.zerotohero.utils.Coordinates;
import fr.iutfbleau.zerotohero.game.Assets;
import fr.iutfbleau.zerotohero.room.Room;
import fr.iutfbleau.zerotohero.room.RoomType;
import fr.iutfbleau.zerotohero.room.connections.Connection;
import fr.iutfbleau.zerotohero.room.connections.ConnectionPosition;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class Minimap extends Widget {
    private static final float DEFAULT_ROOM_SIZE = 26f;
    private static final float DEFAULT_ROOM_PADDING = 4f;
    private static final float DEFAULT_ICON_SIZE = 16f;
    private static final float DEFAULT_CONNECTION_WIDTH = 8f;
//    private static final float DEFAULT_ALPHA = 0.7f;
    private static final Color DEFAULT_CURRENT_ROOM_TINT = new Color(0,1,0,0.7f);
    private static final Color DEFAULT_NOT_VISITED_TINT = new Color(0.3f,0.3f,0.3f,0.5f);
    private static final Color DEFAULT_VISITED_TINT = new Color(1,1,1,0.7f);
    private static final Color DEFAULT_CONNECTION_TINT = new Color(1,1,1,0.7f);

    private final float roomSize, roomPadding, iconSize, connectionWidth;
    private final Texture normalRoom, tallRoomTop, tallRoomPadding, connection;
    private final Color currentRoomTint, notVisitedTint, visitedTint, connectionTint;
    private final Drawable background;
    private final Set<Connection> drawnConnections;
    private int rows, columns;
    private Room[][] rooms;

    public Minimap() {
        this(null);
    }

    public Minimap(Drawable background) {
        this(DEFAULT_ROOM_SIZE, DEFAULT_ROOM_PADDING, DEFAULT_ICON_SIZE, DEFAULT_CONNECTION_WIDTH, DEFAULT_CURRENT_ROOM_TINT, DEFAULT_NOT_VISITED_TINT, DEFAULT_VISITED_TINT, DEFAULT_CONNECTION_TINT, background);
    }

    public Minimap(float roomSize, float roomPadding, float iconSize, float connectionWidth,
                   Color currentRoom, Color notVisited, Color visited, Color connection,
                   Drawable background) {
        this.rows = 1;
        this.columns = 1;
        this.roomSize = roomSize;
        this.roomPadding = roomPadding;
        this.iconSize = iconSize;
        this.connectionWidth = connectionWidth;
        this.currentRoomTint = currentRoom;
        this.notVisitedTint = notVisited;
        this.visitedTint = visited;
        this.connectionTint = connection;
        this.background = background;

        this.rooms = null;
        this.drawnConnections = new HashSet<>();

        Assets assets = ZeroToHero.getAssetManager();
        for(RoomType type : RoomType.values()) {
            String path = type.getMapIconPath();
            if (path != null && !path.equals(""))
                assets.addAsset(path, Texture.class);
        }

        assets.addAsset("gui/minimap/room_normal.png", Texture.class);
        assets.addAsset("gui/minimap/room_tall_top.png", Texture.class);
        assets.addAsset("gui/minimap/room_tall_padding.png", Texture.class);
        assets.addAsset("gui/minimap/connection.png", Texture.class);

        this.normalRoom = assets.getAsset("gui/minimap/room_normal.png", Texture.class);
        this.tallRoomTop = assets.getAsset("gui/minimap/room_tall_top.png", Texture.class);
        this.tallRoomPadding = assets.getAsset("gui/minimap/room_tall_padding.png", Texture.class);
        this.connection = assets.getAsset("gui/minimap/connection.png", Texture.class);
    }

    public void setRooms(Room[][] rooms) {
        Objects.requireNonNull(rooms);
        if (rooms.length == 0 || rooms[0].length == 0)
            throw new IllegalArgumentException("No rooms");

        this.columns = rooms.length;
        this.rows = rooms[0].length;
        this.rooms = rooms;


        // TODO invalidate ???
    }

    @Override
    public float getPrefWidth() {
        return (this.roomSize + this.roomPadding) * this.columns + this.roomPadding;
    }

    @Override
    public float getPrefHeight() {
        return (this.roomSize + this.roomPadding) * this.rows + this.roomPadding;
    }

    @Override
    public void layout() {
        // TODO
    }

    /**
     * If this method is overridden, the super method or {@link #validate()} should be
     * called to ensure the widget is laid out.
     *
     * @param batch
     * @param parentAlpha
     */
    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        Objects.requireNonNull(this.rooms, "No rooms to draw.");

        this.drawBackground(batch, parentAlpha);
        this.drawRooms(batch, parentAlpha);
    }

    private void drawBackground(Batch batch, float alpha) {
        if (this.background == null)
            return;

        batch.setColor(1,1,1, alpha);
        this.background.draw(batch, this.getX(), this.getY(), this.getWidth(), this.getHeight());
        batch.setColor(Color.WHITE);
    }

    private void drawRooms(Batch batch, float alpha) {
        this.drawnConnections.clear();
        for(int columnIndex = 0; columnIndex < this.rooms.length; columnIndex++) {
            final Room[] column = this.rooms[columnIndex];
            for (int rowIndex = 0; rowIndex < column.length; rowIndex++) {
                final Room room = column[rowIndex];
                if (! this.shouldDraw(room))
                    continue;


                boolean isCurrentRoom = ZeroToHero.getPlayer().getRoom().equals(room);
                if (isCurrentRoom)
                    batch.setColor(currentRoomTint.r, currentRoomTint.g, currentRoomTint.b, alpha * currentRoomTint.a);
                else if ( ! room.isVisited() )
                    batch.setColor(notVisitedTint.r, notVisitedTint.g, notVisitedTint.b, alpha * notVisitedTint.a);
                else
                    batch.setColor(visitedTint.r, visitedTint.g, visitedTint.b, alpha * visitedTint.a);

                if( room.isTall() ) {
                    this.drawTallRoom(batch, room, rowIndex, columnIndex);
                } else {
                    this.drawNormalRoom(batch, room, rowIndex, columnIndex);
                }

                batch.setColor(this.connectionTint);

                for(Map.Entry<ConnectionPosition, Connection> entry: room.getConnections().entrySet()) {
                    if (! this.drawnConnections.contains(entry.getValue()) && shouldDraw(entry.getValue().getOtherRoom(room))) {
                        int row = rowIndex;
                        int endColumn;
                        if (room.isTall() &&  ! entry.getKey().name().contains("BOTTOM_")) {
                            row += 1;
                        }
                        if (entry.getKey().name().contains("LEFT"))
                            endColumn = columnIndex - 1;
                        else
                            endColumn = columnIndex + 1;

                        this.drawConnection(batch, row, columnIndex, endColumn );
                        this.drawnConnections.add(entry.getValue());
                    }
                }

                batch.setColor(Color.WHITE);
            }
        }
    }

    private boolean shouldDraw(Room room) {
        if (room == null) return false;
        if (room.isVisited()) return true;

        for (Connection conn: room.getConnections().values())
            if (conn.getOtherRoom(room).isVisited())
                return true;


        return false;
    }

    private void drawNormalRoom(Batch batch, Room room, int row, int column) {
        Coordinates pos = this.indexToPosition(row, column);
        // room background
        batch.draw(this.normalRoom, pos.getX(), pos.getY(), this.roomSize, this.roomSize);
        // icon
        this.drawRoomType(batch, room.getMapIconPath(),
                          pos.getX() + this.roomSize / 2f,
                          pos.getY() + this.roomSize / 2f);
    }

    private void drawTallRoom(Batch batch, Room room, int row, int column) {
        Coordinates bottomPos = this.indexToPosition(row, column);

        // grid cells
        batch.draw(this.tallRoomTop, bottomPos.getX(), bottomPos.getY(),
                   this.roomSize, this.roomSize, 0, 0,
                   this.tallRoomTop.getWidth(), this.tallRoomTop.getHeight(),
                   false, true);
        batch.draw(this.tallRoomTop, bottomPos.getX(), bottomPos.getY() + this.roomSize + this.roomPadding,
                   this.roomSize, this.roomSize, 0, 0,
                   this.tallRoomTop.getWidth(), this.tallRoomTop.getHeight(),
                   false, false);
        // padding
        batch.draw(this.tallRoomPadding, bottomPos.getX(), bottomPos.getY() + this.roomSize,
                   this.roomSize, this.roomPadding);
        // icon
        this.drawRoomType(batch, room.getMapIconPath(),
                          bottomPos.getX() + this.roomSize / 2f,
                          bottomPos.getY() + this.roomSize  + this.roomPadding / 2f);
    }

    private void drawRoomType(Batch batch, String iconPath, float centerX, float centerY) {
        if (iconPath == null || iconPath.equals(""))
            return;

        Color oldColor = batch.getColor().cpy();
        batch.setColor(Color.WHITE);
        Texture texture = ZeroToHero.getAssetManager().getAsset(iconPath, Texture.class);
        batch.draw(texture, centerX - this.iconSize / 2f,centerY - this.iconSize / 2f,
                   this.iconSize, this.iconSize);
        batch.setColor(oldColor);
    }

    private void drawConnection(Batch batch, int row, int startColumn, int endColumn) {
        if (startColumn == endColumn)
            throw new IllegalArgumentException("Cannot draw this connection");

        Coordinates start = this.indexToPosition(row, startColumn);
//        Coordinates end = this.indexToPosition(row, startColumn);

        start.setY(start.getY() + (this.roomSize - this.connectionWidth) / 2f);

        if (startColumn < endColumn)
            start.setX(start.getX() + this.roomSize);
        else
            start.setX(start.getX() - this.roomPadding);

        batch.draw(this.connection, start.getX(), start.getY(), this.roomPadding, this.connectionWidth);
    }

    private Coordinates indexToPosition(int rowIndex, int columnIndex) {
        return new Coordinates(
                this.getX() + this.roomPadding + columnIndex * (this.roomSize + this.roomPadding),
                this.getY() + this.getHeight() - (this.roomPadding + this.roomSize)  * (this.rows - rowIndex)
        );
    }
}
