#define NeighborCount 64
#define SolverIterations 1

float3 GetNeighborVectorValue(global const float *Neighbors, uint ParticleIndex, uint ParticleSize, uint NeighborIndex, uint ElementOffset) {
    //Offset Results
    //0 = Position
    //1 = PastPosition
    //2 = Velocity
    //3 = PastAcceleration
    //4 = Force

    float3 NeighborVector = (float3)0;
    NeighborVector.x = Neighbors[(NeighborIndex * (NeighborCount * ParticleSize)) + (ParticleSize * NeighborIndex) + (ElementOffset * 3 + 0)];
    NeighborVector.y = Neighbors[(NeighborIndex * (NeighborCount * ParticleSize)) + (ParticleSize * NeighborIndex) + (ElementOffset * 3 + 1)];
    NeighborVector.z = Neighbors[(NeighborIndex * (NeighborCount * ParticleSize)) + (ParticleSize * NeighborIndex) + (ElementOffset * 3 + 2)];
    return NeighborVector;
}

float GetNeighborScalarValue(global const float *Neighbors, uint ParticleIndex, uint ParticleSize, uint NeighborIndex, uint ElementOffset) {
    //Offset Results
    //0 = Pressure
    //1 = Density
    
    return Neighbors[(NeighborIndex * (NeighborCount * ParticleSize)) + (ParticleSize * NeighborIndex) + (15 + ElementOffset)];
}

float Poly6(float3 Point1, float3 Point2, global const float* Preferences) {
    float Distance = fast_distance(Point1, Point2);
    
    if (Preferences[2] < Distance) {
        return 0;
    }

    float Coefficient = 315 / (64 * M_PI_F * Preferences[5]);

    return (float)(Coefficient * pow(Preferences[2] - Distance, 3));
}

float3 SpikyGrad(float3 Point1, float3 Point2, global const float *Preferences) {
    float Distance = fast_distance(Point1, Point2);

    if (Preferences[2] < Distance) {
        return (float3)0;
    }

    float Coefficient = 45 / (M_PI_F * Preferences[4]);

    float3 NormalizedDeltaPos = fast_normalize(Point1 - Point2);
    NormalizedDeltaPos *= (float)pow(Preferences[2] - Distance, 2);

    return NormalizedDeltaPos * -Coefficient;
}

float Laplacian(float3 Point1, float3 Point2, global const float *Preferences) {
    float Distance = fast_distance(Point1, Point2);

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

//NeighborPosition = GetNeighborVectorValue(Neighbors, ParticleIndex, ParticleSize, NeighborIndex, 0);
//NeighborVelocity = GetNeighborVectorValue(Neighbors, ParticleIndex, ParticleSize, NeighborIndex, 2);
//NeighborPastAcceleration = GetNeighborVectorValue(Neighbors, ParticleIndex, ParticleSize, NeighborIndex, 3);
//NeighborForce = GetNeighborVectorValue(Neighbors, ParticleIndex, ParticleSize, NeighborIndex, 4);

kernel void tester( global const float *Particle, global const float *Neighbors, global const float *Preferences, global float *Out ) {
    uint GlobalIndex = get_global_id(0);
    uint ParticleSize = 17;
    uint ParticleIndex = GlobalIndex * ParticleSize;

    float3 Position = (float3)0;
    float3 Velocity = (float3)0;
    float3 PastAcceleration = (float3)0;
    float3 Force = (float3)0;
    float Pressure = 0;
    float Density = 0;

    Position.x = Particle[ParticleIndex + 0];
    Position.y = Particle[ParticleIndex + 1];
    Position.z = Particle[ParticleIndex + 2];
    
    Velocity.x = Particle[ParticleIndex + 6];
    Velocity.y = Particle[ParticleIndex + 7];
    Velocity.z = Particle[ParticleIndex + 8];
    
    PastAcceleration.x = Particle[ParticleIndex + 9];
    PastAcceleration.y = Particle[ParticleIndex + 10];
    PastAcceleration.z = Particle[ParticleIndex + 11];
    
    Force.x = Particle[ParticleIndex + 12];
    Force.y = Particle[ParticleIndex + 13];
    Force.z = Particle[ParticleIndex + 14];
    
    Pressure = Particle[ParticleIndex + 15];
    Density = Particle[ParticleIndex + 16];

    float3 NeighborPosition = (float3)0;
    float3 NeighborVelocity = (float3)0;
    float NeighborDensity = 0;
    float NeighborPressure = 0;

    //Calculate Density & Pressure
    for (uint NeighborIndex = 0; NeighborIndex < NeighborCount; NeighborIndex++) {

        if (IsNaN(GetNeighborScalarValue( Neighbors, ParticleIndex, ParticleSize, NeighborIndex, 0 ))) {
            break;
        }

        NeighborPosition = GetNeighborVectorValue(Neighbors, ParticleIndex, ParticleSize, NeighborIndex, 0);

        if ( fast_distance( Position, NeighborPosition ) <= Preferences[0] ) {
            Density += Preferences[1] * Poly6(
                Position,
                NeighborPosition,
                Preferences
            );
        }
    }

    Density = max(Density, Preferences[6]);

    Pressure = Preferences[7] * (Density - Preferences[6]);

    //Calculate Force
    float3 PressureGradient = (float3)0;
    float3 ViscosityGradient = (float3)0;

     for (uint NeighborIndex = 0; NeighborIndex < NeighborCount; NeighborIndex++) {

        if (IsNaN(GetNeighborScalarValue( Neighbors, ParticleIndex, ParticleSize, NeighborIndex, 0 ))) {
            break;
        }

        NeighborPosition = GetNeighborVectorValue(Neighbors, ParticleIndex, ParticleSize, NeighborIndex, 0);
        NeighborVelocity = GetNeighborVectorValue(Neighbors, ParticleIndex, ParticleSize, NeighborIndex, 2);
        NeighborPressure = GetNeighborScalarValue(Neighbors, ParticleIndex, ParticleSize, NeighborIndex, 0);
        NeighborDensity = GetNeighborScalarValue(Neighbors, ParticleIndex, ParticleSize, NeighborIndex, 1);

        PressureGradient +=
            SpikyGrad(Position, NeighborPosition, Preferences)
            * (
                -Preferences[1]
                * (
                    Pressure + NeighborPressure
                    / 2 * Density * NeighborDensity
                )
            )
        ;

        ViscosityGradient +=
            Velocity - NeighborVelocity
            * (
                -Preferences[8]
                * (
                    Preferences[1] / Density
                    * Laplacian(Position, NeighborPosition, Preferences)
                )
            )
        ;
    }

    Force += PressureGradient + ViscosityGradient;

    //Add Gravity
    Force.x += Preferences[9];
    Force.y += Preferences[10];
    Force.z += Preferences[11];

    //Integrate Particle
    Velocity += (PastAcceleration + Force) * (float)0.5 * Preferences[15];

    float3 DeltaPosition = (Velocity * Preferences[15]) * (Force * Preferences[15] * Preferences[15] * (float)0.5);

    Position += DeltaPosition;
    PastAcceleration = Force;

    //Write to Memory Object
    Out[ParticleIndex + 0] = Position.x;
    Out[ParticleIndex + 1] = Position.y;
    Out[ParticleIndex + 2] = Position.z;

    Out[ParticleIndex + 3] = 0;
    Out[ParticleIndex + 4] = 0;
    Out[ParticleIndex + 5] = 0;

    Out[ParticleIndex + 6] = Velocity.x;
    Out[ParticleIndex + 7] = Velocity.y;
    Out[ParticleIndex + 8] = Velocity.z;

    Out[ParticleIndex + 9] = PastAcceleration.x;
    Out[ParticleIndex + 10] = PastAcceleration.y;
    Out[ParticleIndex + 11] = PastAcceleration.z;

    Out[ParticleIndex + 12] = Force.x;
    Out[ParticleIndex + 13] = Force.y;
    Out[ParticleIndex + 14] = Force.z;

    Out[ParticleIndex + 15] = Pressure;

    Out[ParticleIndex + 16] = Density;

    /*PositionsOut[GlobalIndex * 3 + 0] = Position.x;
    PositionsOut[GlobalIndex * 3 + 1] = Position.y;
    PositionsOut[GlobalIndex * 3 + 2] = Position.z;*/
}