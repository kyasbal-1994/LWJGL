
import org.lwjgl.egl.EGL
import org.lwjgl.opengl.GL.setCapabilities
import org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT
import org.lwjgl.opengl.GL11.glClear
import org.lwjgl.opengl.GL11.glClearColor
import org.lwjgl.opengl.GL11.GL_RENDERER
import org.lwjgl.opengl.GL11.glGetString
import org.lwjgl.opengl.GL11.GL_VERSION
import org.lwjgl.opengl.GL11.GL_VENDOR
import org.lwjgl.system.MemoryStack.stackPush

import org.lwjgl.egl.EGL10.*
import org.lwjgl.egl.EGLCapabilities
import org.lwjgl.glfw.Callbacks.*
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.glfw.GLFWNativeEGL.*
import org.lwjgl.opengles.GLES
import org.lwjgl.opengles.GLES20.*
import org.lwjgl.opengles.GLESCapabilities
import org.lwjgl.system.MemoryStack.*
import org.lwjgl.system.MemoryUtil.*


fun main(){
    GLFWErrorCallback.createPrint().set()
    if (!glfwInit()) {
        throw IllegalStateException("Unable to initialize glfw")
    }

    glfwDefaultWindowHints()
    glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE)
    glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE)

    // GLFW setup for EGL & OpenGL ES
    glfwWindowHint(GLFW_CONTEXT_CREATION_API, GLFW_EGL_CONTEXT_API)
    glfwWindowHint(GLFW_CLIENT_API, GLFW_OPENGL_ES_API)
    glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 2)
    glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 0)

    val WIDTH = 300
    val HEIGHT = 300

    val window = glfwCreateWindow(WIDTH, HEIGHT, "GLFW EGL/OpenGL ES Demo", NULL, NULL)
    if (window == NULL) {
        throw RuntimeException("Failed to create the GLFW window")
    }

    glfwSetKeyCallback(window) { windowHnd, key, scancode, action, mods ->
        if (action === GLFW_RELEASE && key === GLFW_KEY_ESCAPE) {
            glfwSetWindowShouldClose(windowHnd, true)
        }
    }

    // EGL capabilities
    val dpy = glfwGetEGLDisplay()

    lateinit var egl: EGLCapabilities
    stackPush().use { stack ->
        val major = stack.mallocInt(1)
        val minor = stack.mallocInt(1)

        if (!eglInitialize(dpy, major, minor)) {
            throw IllegalStateException(String.format("Failed to initialize EGL [0x%X]", eglGetError()))
        }

        egl = EGL.createDisplayCapabilities(dpy, major.get(0), minor.get(0))
    }

    try {
        println("EGL Capabilities:")
        for (f in EGLCapabilities::class.java!!.getFields()) {
            if (f.getType() === Boolean::class.javaPrimitiveType) {
                if (f.get(egl).equals(java.lang.Boolean.TRUE)) {
                    System.out.println("\t" + f.getName())
                }
            }
        }
    } catch (e: IllegalAccessException) {
        e.printStackTrace()
    }


    // OpenGL ES capabilities
    glfwMakeContextCurrent(window)
    val gles = GLES.createCapabilities()

    try {
        println("OpenGL ES Capabilities:")
        for (f in GLESCapabilities::class.java!!.getFields()) {
            if (f.getType() === Boolean::class.javaPrimitiveType) {
                if (f.get(gles).equals(java.lang.Boolean.TRUE)) {
                    System.out.println("\t" + f.getName())
                }
            }
        }
    } catch (e: IllegalAccessException) {
        e.printStackTrace()
    }


    println("GL_VENDOR: " + glGetString(GL_VENDOR)!!)
    println("GL_VERSION: " + glGetString(GL_VERSION)!!)
    println("GL_RENDERER: " + glGetString(GL_RENDERER)!!)

    // Render with OpenGL ES
    glfwShowWindow(window)

    glClearColor(0.0f, 0.5f, 1.0f, 0.0f)
    while (!glfwWindowShouldClose(window)) {
        glfwPollEvents()

        glClear(GL_COLOR_BUFFER_BIT)
        glfwSwapBuffers(window)
    }

    GLES.setCapabilities(null)

    glfwFreeCallbacks(window)
    glfwTerminate()
}