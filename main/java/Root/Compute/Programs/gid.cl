kernel void gid(global const float *in, global float *out) {
    unsigned int Index = get_global_id(0);
    unsigned int Size = get_global_size(0);

    for (int i = 0; i < gs; i++) {
        out[(gid*gs)+i] = (float)gid;
    }

}