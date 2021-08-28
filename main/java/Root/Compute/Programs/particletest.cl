kernel void particletest(global const float *particledata, global float *result) {
    unsigned int gid = get_global_id(0);
    unsigned int index = 9 * gid;

    float3 mainparticle = (float3)(particledata[index], particledata[index + 1], particledata[index + 2]);
    float3 total = (float3)0;

    for (unsigned int i = 1; i < (9 / 3) - 1; i++) {

        unsigned int offset = i * 3;
        float3 currentparticle = (float3)(particledata[index + offset + 0], particledata[index + offset + 1], particledata[index + offset + 2]);

        total += currentparticle;

    }

    result[gid+0] = total.x;
    result[gid+1] = total.y;
    result[gid+2] = total.z;

}