package com.game.engine.audio;

import com.game.caches.GlobalCache;
import com.game.engine.scene.audio.AudioBufferObject;
import com.game.engine.scene.audio.AudioSourceObject;
import org.joml.Vector3f;
import org.lwjgl.openal.*;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;

public class AudioManager {
  private final HashMap<String, AudioSourceObject> sources;
  private final ALCapabilities capabilities;
  private final long device;
  private final long context;

  public AudioManager() {
    sources = new HashMap<>();

    device = ALC11.alcOpenDevice((ByteBuffer) null);

    if (device == MemoryUtil.NULL) throw new IllegalStateException("Failed to open the OpenAL device.");

    ALCCapabilities deviceCapabilities = ALC.createCapabilities(device);
    if (!deviceCapabilities.OpenALC11) throw new IllegalStateException();

    context = ALC11.alcCreateContext(device, (IntBuffer) null);
    if (context == MemoryUtil.NULL) throw new IllegalStateException("Failed to create OpenAL context.");

    boolean useThreadLocalContext = deviceCapabilities.ALC_EXT_thread_local_context && EXTThreadLocalContext.alcSetThreadContext(context);

    if (!useThreadLocalContext && !ALC11.alcMakeContextCurrent(context)) throw new IllegalStateException();

    capabilities = AL.createCapabilities(deviceCapabilities, MemoryUtil::memCallocPointer);
  }

  public AudioSourceObject load(String key, String path, Vector3f position) {
    AudioBufferObject abo = GlobalCache.instance().audioBuffer(path);
    AudioSourceObject aso = get(key, position);
    aso.buffer(abo);

    return aso;
  }

  public AudioSourceObject get(String key) {
    return sources.computeIfAbsent(key, AudioSourceObject::new);
  }

  public AudioSourceObject get(String key, Vector3f position) {
    AudioSourceObject aso = get(key);
    aso.moveTo(position);

    return aso;
  }

  public void setAttenuationModel(int model) { AL11.alDistanceModel(model); }

  public void dispose() {
    sources.values().forEach(AudioSourceObject::dispose);

    if (capabilities != null) MemoryUtil.memFree(capabilities.getAddressBuffer());
    if (context != MemoryUtil.NULL) ALC11.alcDestroyContext(context);
    if (device != MemoryUtil.NULL) ALC11.alcCloseDevice(device);
  }
}
