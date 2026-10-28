package cz.vse.pyraquest.data;

import cz.vse.pyraquest.domain.Npc;
import cz.vse.pyraquest.domain.Room;
import cz.vse.pyraquest.domain.Skeleton;
import cz.vse.pyraquest.game.GameState;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WorldLoaderTest {

    private final GameState state = WorldLoader.load(1L);

    @Test
    void buildsAllTwelveRooms() {
        assertEquals(12, state.getRooms().size());
    }

    @Test
    void playerStartsInEntrance() {
        assertEquals("vstup", state.getPlayer().getCurrentRoom().getCode());
    }

    @Test
    void exitsAreTwoWay() {
        for (Room room : state.getRooms().values()) {
            for (Room exit : room.getExits().values()) {
                assertTrue(exit.getExits().containsKey(room.getCode()),
                        room.getCode() + " <-> " + exit.getCode());
            }
        }
    }

    @Test
    void entryConditionsAreSet() {
        assertEquals("dark", state.getRoom("kostnice").getEntryCondition());
        assertEquals("wall", state.getRoom("hrobka").getEntryCondition());
        assertNull(state.getRoom("chodba").getEntryCondition());
    }

    @Test
    void itemsAreOnTheirStartingPlaces() {
        assertNotNull(state.getRoom("chodba").getItem("nuz"));
        assertNotNull(state.getRoom("pokladnice").getItem("kresadlo"));
        assertTrue(state.getRoom("komnata").getItem("truhla").isLocked());
    }

    @Test
    void hiddenItemsAreInsideContainers() {
        Room bones = state.getRoom("kostnice");
        assertNull(bones.getItem("kost"));
        assertEquals(1, bones.getItem("hromada-kosti").getContents().size());
    }

    @Test
    void skeletonHoldsTheKey() {
        Npc skeleton = state.getRoom("sal").getNpc("kostlivec");
        assertInstanceOf(Skeleton.class, skeleton);
        assertEquals("klic", skeleton.getItems().get(0).getCode());
    }

    @Test
    void eachLoadBuildsFreshWorld() {
        assertNotSame(WorldLoader.load(1L).getRoom("chodba"), state.getRoom("chodba"));
    }
}