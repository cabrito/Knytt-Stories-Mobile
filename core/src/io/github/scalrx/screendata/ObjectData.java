/*
 * ObjectData.java
 * A small container for gathering and storing object data, to be used with KsmScreen.
 * Created by: scalr on 4/19/2019.
 *
 * Knytt Stories Mobile
 * https://github.com/scalrx
 * Copyright (c) 2019 by scalr.
 *
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR  A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package io.github.scalrx.screendata;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import io.github.scalrx.world.World;

public class ObjectData {

    // Helpful constants
    private final World world;
    private byte[][][] objectData = new byte[4][10][50];

    public ObjectData(final World world) {
        this.world = world;
    }

    public void placeObjects(int xID, int yID) {
        if (world.getMap().screenOffsetExists(xID, yID)) {
            generateData(xID, yID);
        } else {
            // Otherwise, there's nothing for us to do since we're in the VOID
        }
    }

    private void generateData(int xID, int yID) {
        final int LAYER_SIZE = 250;
        final int NUMBER_OF_LAYERS = 4;

        // Open Map.bin.raw as a byte array
        FileHandle mapFile = Gdx.files.external(world.getFiles().mapBin(true));
        byte[] mapFileBytes = mapFile.readBytes();

        // Seek the specific location in the Map file
        int objectDataLocation = world.getMap().getScreenOffset(xID, yID) + (LAYER_SIZE * NUMBER_OF_LAYERS);
        int cursorPosition = objectDataLocation;

        // Copy in byte data for layers 4 - 7
        for (int objLayer = 4; objLayer <= 7; objLayer++) {
            // Read object number
            for (int row = 0; row < 10; row++) {
                for (int column = 0; column < 25; column++, cursorPosition++) {
                    objectData[objLayer - 4][row][column] = mapFileBytes[cursorPosition];
                }
            }

            // Read bank number
            for (int row = 0; row < 10; row++) {
                for (int column = 25; column < 50; column++, cursorPosition++) {
                    objectData[objLayer - 4][row][column] = mapFileBytes[cursorPosition];
                }
            }
        }
    }

    public void render() {

    }

    public void dispose() {
        // Unload/dispose the getResources since we're not using them anymore
    }
}