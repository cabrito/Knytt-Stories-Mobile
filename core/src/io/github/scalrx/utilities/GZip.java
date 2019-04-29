/*
 * GZip.java
 * Utility for (de)compression of GZip archives (such as Map.bin).
 * Created by: scalr on 3/24/2019.
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

package io.github.scalrx.utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public final class GZip {

	public static void decompress(final String filepath) {
		byte[] buffer = new byte[1024];
		try {
			FileHandle fh = Gdx.files.external(filepath);
			GZIPInputStream input = new GZIPInputStream(fh.read());

			FileHandle output = Gdx.files.external(filepath + ".raw");

			int totalSize;
			while ((totalSize = input.read(buffer)) > 0) {
				output.writeBytes(buffer, 0, totalSize, true);
			}
			input.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void compress(final String filepath) {
		byte[] buffer = new byte[1024];
		try {
			GZIPOutputStream output = new GZIPOutputStream(new FileOutputStream(filepath + ".gz"));

			FileInputStream input = new FileInputStream(filepath);

			int totalSize;
			while ((totalSize = input.read(buffer)) > 0) {
				output.write(buffer, 0, totalSize);
			}

			input.close();
			output.finish();
			output.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
