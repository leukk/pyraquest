package cz.vse.pyraquest.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EnemyTest {

    @Test
    void newEnemyIsAlive() {
        Enemy enemy = new Enemy("skorpion", "Škorpion", "pochoden");
        assertTrue(enemy.isAlive());
    }

    @Test
    void killedEnemyIsNotAlive() {
        Enemy enemy = new Enemy("skorpion", "Škorpion", "pochoden");
        enemy.kill();
        assertFalse(enemy.isAlive());
    }
}