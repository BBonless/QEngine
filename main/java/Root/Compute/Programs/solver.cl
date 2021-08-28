#define NeighborCount 64
#define SolverIterations 1

float3 GetTargetPosition(global const float *Particle, uint GlobalIndex, uint Size) {
    float3 TargetPosition = (float3)0;
    TargetPosition.x = Particle[GlobalIndex + 0];
    TargetPosition.y = Particle[GlobalIndex + 1];
    TargetPosition.z = Particle[GlobalIndex + 2];
    return TargetPosition;
}

float3 GetTargetPastPosition(global const float *Particle, uint GlobalIndex, uint Size) {
    float3 TargetPosition = (float3)0;
    TargetPosition.x = Particle[GlobalIndex + 3];
    TargetPosition.y = Particle[GlobalIndex + 4];
    TargetPosition.z = Particle[GlobalIndex + 5];
    return TargetPosition;
}

float3 GetTargetVelocity(global const float *Particle, uint GlobalIndex, uint Size) {
    float3 TargetVelocity = (float3)0;
    TargetVelocity.x = Particle[GlobalIndex + 6];
    TargetVelocity.y = Particle[GlobalIndex + 7];
    TargetVelocity.z = Particle[GlobalIndex + 8];
    return TargetVelocity;
}

float3 GetTargetPastAcceleration(global const float *Particle, uint GlobalIndex, uint Size) {
    float3 TargetPastAcceleration = (float3)0;
    TargetPastAcceleration.x = Particle[GlobalIndex + 9];
    TargetPastAcceleration.y = Particle[GlobalIndex + 10];
    TargetPastAcceleration.z = Particle[GlobalIndex + 11];
    return TargetPastAcceleration;
}

float3 GetTargetForce(global const float *Particle, uint GlobalIndex, uint Size) {
    float3 TargetForce = (float3)0;
    TargetForce.x = Particle[GlobalIndex + 12];
    TargetForce.y = Particle[GlobalIndex + 13];
    TargetForce.z = Particle[GlobalIndex + 14];
    return TargetForce;
}

float GetTargetPressure(global const float *Particle, uint GlobalIndex, uint Size) {
    return Particle[GlobalIndex + 15];
}

float GetTargetDensity(global const float *Particle, uint GlobalIndex, uint Size) {
    return Particle[GlobalIndex + 16];
}

float3 GetNeighborPosition(global const float *Neighbors, uint Index, uint Size, uint NeighborIndex) {
    float3 NeighborPosition = (float3)0;
    NeighborPosition.x = Neighbors[(Index * (NeighborCount * Size)) + (Size * NeighborIndex) + 0];
    NeighborPosition.y = Neighbors[(Index * (NeighborCount * Size)) + (Size * NeighborIndex) + 1];
    NeighborPosition.z = Neighbors[(Index * (NeighborCount * Size)) + (Size * NeighborIndex) + 2];
    return NeighborPosition;
}

float3 GetNeighborPastPosition(global const float *Neighbors, uint Index, uint Size, uint NeighborIndex) {
    float3 NeighborPosition = (float3)0;
    NeighborPosition.x = Neighbors[(Index * (NeighborCount * Size)) + (Size * NeighborIndex) + 3];
    NeighborPosition.y = Neighbors[(Index * (NeighborCount * Size)) + (Size * NeighborIndex) + 4];
    NeighborPosition.z = Neighbors[(Index * (NeighborCount * Size)) + (Size * NeighborIndex) + 5];
    return NeighborPosition;
}

float3 GetNeighborVelocity(global const float *Neighbors, uint Index, uint Size, uint NeighborIndex) {
    float3 NeighborVelocity = (float3)0;
    NeighborVelocity.x = Neighbors[(Index * (NeighborCount * Size)) + (Size * NeighborIndex) + 6];
    NeighborVelocity.y = Neighbors[(Index * (NeighborCount * Size)) + (Size * NeighborIndex) + 7];
    NeighborVelocity.z = Neighbors[(Index * (NeighborCount * Size)) + (Size * NeighborIndex) + 8];
    return NeighborVelocity;
}

float3 GetNeighborPastAcceleration(global const float *Neighbors, uint Index, uint Size, uint NeighborIndex) {
    float3 NeighborPastAcceleration = (float3)0;
    NeighborPastAcceleration.x = Neighbors[(Index * (NeighborCount * Size)) + (Size * NeighborIndex) + 9];
    NeighborPastAcceleration.y = Neighbors[(Index * (NeighborCount * Size)) + (Size * NeighborIndex) + 10];
    NeighborPastAcceleration.z = Neighbors[(Index * (NeighborCount * Size)) + (Size * NeighborIndex) + 11];
    return NeighborPastAcceleration;
}

float3 GetNeighborForce(global const float *Neighbors, uint Index, uint Size, uint NeighborIndex) {
    float3 NeighborForce = (float3)0;
    NeighborForce.x = Neighbors[(Index * (NeighborCount * Size)) + (Size * NeighborIndex) + 12];
    NeighborForce.y = Neighbors[(Index * (NeighborCount * Size)) + (Size * NeighborIndex) + 13];
    NeighborForce.z = Neighbors[(Index * (NeighborCount * Size)) + (Size * NeighborIndex) + 14];
    return NeighborForce;
}

float GetNeighborPressure(global const float *Neighbors, uint Index, uint Size, uint NeighborIndex) {
    return Neighbors[(Index * (NeighborCount * Size)) + (Size * NeighborIndex) + 15];
}

float GetNeighborDensity(global const float *Neighbors, uint Index, uint Size, uint NeighborIndex) {
    return Neighbors[(Index * (NeighborCount * Size)) + (Size * NeighborIndex) + 16];
}

float Poly6(float3 Vec1, float3 Vec2, float *Preferences) {
    float Distance = fast_distance(Vec1, Vec2);

    if (Preferences[2] < Distance) {
        return 0;
    }

    float Coefficient = 315 / (64 * M_PI_F * Preferences[5]);

    return (float)(Coefficient * pow(Preferences[2] - Distance, 3));
}

float3 SpikyGrad(float3 Vec1, float3 Vec2, float *Preferences) {
    float Distance = fast_distance(Vec1, Vec2);

    if (Preferences[2] < Distance) {
        return (float3)0;
    }

    float Coefficient = 45 / (M_PI_F * Preferences[4]);

    float3 NormalizedDeltaPos = fast_normalize(Vec1 - Vec2);
    NormalizedDeltaPos *= (float)pow(Preferences[2] - Distance, 2);

    return NormalizedDeltaPos * -Coefficient;
}

float Laplacian(float3 Vec1, float3 Vec2, float *Preferences) {
    float Distance = fast_distance(Vec1, Vec2);

    if (Preferences[2] < Distance) {
        return 0;
    }

    float Coefficient = 45 / (M_PI_F * Preferences[4]);

    return Coefficient * (Preferences[2] - Distance);
}

bool IsNaN(float Number) {
    return Number != Number;
}

//Preferences[0] = Smoothing Radius NOT SQUARED
//Preferences[1] = Particle Mass
//Preferences[2,3,4,5] = Kernel Radius ^ 1,2,6,9
//Preferences[6] = Rest Density;
//Preferences[7] = Stiffness;
//Preferences[8] = Viscosity;
//Preferences[9,10,11] = Gravity x,y,z;
//Preferences[12,13,14] = Bounds x,y,z;
//Preferences[15] = Timestep;

kernel void solver( global const float *Particle, global const float *Neighbors, global const float *Preferences, global float *Out ) {
    unsigned int GlobalIndex = get_global_id(0);
    unsigned int Size = 17;

    float Density = 0;
    float Pressure = 0;
    float3 Position = GetTargetPosition(Particle, GlobalIndex, Size);
    float3 Velocity = GetTargetVelocity(Particle, GlobalIndex, Size);
    float3 Force = (float3)0;
    float3 PastAcceleration = (float3)0;
    float3 PressureGradient = (float3)0;
    float3 ViscosityGradient = (float3)0;

    for (uint i = 0; i < SolverIterations; i++) {
        for (uint NeighborIndex = 0; NeighborIndex < NeighborCount; NeighborIndex++) {
            if ( fast_distance( GetTargetPosition(Particle, GlobalIndex, Size), GetNeighborPosition(Neighbors, GlobalIndex, Size, NeighborIndex) ) <= Preferences[0] ) {
                Density += Preferences[1] * Poly6(
                    GetTargetPosition(Particle, GlobalIndex, Size),
                    GetNeighborPosition(Neighbors, GlobalIndex, Size, NeighborIndex),
                    Preferences
                );
            }
        }

        if (Density < Preferences[6]) {
            Density = Preferences[6];
        }


        Pressure = Preferences[7] * (Density - Preferences[6]);

        //Calculate Net Force
        for (uint NeighborIndex = 0; NeighborIndex < NeighborCount; NeighborIndex++) {

            if (IsNaN(GetNeighborDensity(Neighbors, GlobalIndex, Size, NeighborIndex))) {
                break;
            }

            float PressureDividend = Pressure + GetNeighborPressure(Neighbors, GlobalIndex, Size, NeighborIndex);
            float PressureDivisor = 2 * Density * GetNeighborDensity(Neighbors, GlobalIndex, Size, NeighborIndex);

            PressureGradient += SpikyGrad(
                GetTargetPosition(Particle, GlobalIndex, Size),
                GetNeighborPosition(Neighbors, GlobalIndex, Size, NeighborIndex),
                Preferences
            ) * (-Preferences[1] * (PressureDividend / PressureDivisor));

            float3 DeltaVelocity =
                GetTargetVelocity(Particle, GlobalIndex, Size) -
                GetNeighborVelocity(Neighbors, GlobalIndex, Size, NeighborIndex);

            ViscosityGradient += DeltaVelocity * (-Preferences[8] * (Preferences[1] / Density)
                              *     Laplacian(GetTargetPosition(Particle, GlobalIndex, Size),
                                    GetNeighborPosition(Neighbors, GlobalIndex, Size, NeighborIndex),
                                    Preferences)
            );

        }

        Force += PressureGradient + ViscosityGradient;
        Force.x += Preferences[9];
        Force.y += Preferences[10];
        Force.z += Preferences[11];
    }


    Velocity = GetTargetVelocity(Particle, GlobalIndex, Size);
    Velocity += (GetTargetPastAcceleration(Particle, GlobalIndex, Size) + Force) * (float)0.5 * Preferences[15];

    float3 DeltaPosition = (float3)0;
    DeltaPosition += (Velocity * Preferences[15]) * (Force * Preferences[15] * Preferences[15] * (float)0.5);

    Position += DeltaPosition;
    PastAcceleration = Force;

    Out[GlobalIndex + 0] = Position.x;
    Out[GlobalIndex + 1] = Position.y;
    Out[GlobalIndex + 2] = Position.z;

    Out[GlobalIndex + 3] = 0;
    Out[GlobalIndex + 4] = 0;
    Out[GlobalIndex + 5] = 0;

    Out[GlobalIndex + 6] = Velocity.x;
    Out[GlobalIndex + 7] = Velocity.y;
    Out[GlobalIndex + 8] = Velocity.z;

    Out[GlobalIndex + 9] = PastAcceleration.x;
    Out[GlobalIndex + 10] = PastAcceleration.y;
    Out[GlobalIndex + 11] = PastAcceleration.z;

    Out[GlobalIndex + 12] = Force.x;
    Out[GlobalIndex + 13] = Force.y;
    Out[GlobalIndex + 14] = Force.z;

    Out[GlobalIndex + 15] = Pressure;

    Out[GlobalIndex + 16] = Density;

    //Out[GlobalIndex] = GlobalIndex;

}
