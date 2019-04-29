package io.github.scalrx.world;

import io.github.scalrx.KsmFiles;

/*
 * World.java
 * All of the relevant information for the current world that's loaded.
 * Created by: scalr on 3/30/2019.
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

public class World {
    // World information
    private String worldName;
    private String author;
    private int size;

    // Map information
    private MapFile map;
    private final KsmFiles files;

    public World(final KsmFiles files) {
        // Initialize the files directory specific to the specified world
        this.files = files;
    }

    public String getWorldName() {
        return worldName;
    }

	public void setWorldName(String worldName) {
        this.worldName = worldName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void initMap() {
        files.setWorldDir(author, worldName);
        map = new MapFile(files);
    }

    public MapFile getMap() {
        return map;
    }

    public KsmFiles getFiles() {
        return files;
    }
}
