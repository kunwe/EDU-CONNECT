package com.example.educonnect;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation"
})
public final class TimetableRepository_Factory implements Factory<TimetableRepository> {
  private final Provider<GeminiApi> geminiApiProvider;

  private final Provider<FirebaseFirestore> firestoreProvider;

  private final Provider<FirebaseAuth> authProvider;

  public TimetableRepository_Factory(Provider<GeminiApi> geminiApiProvider,
      Provider<FirebaseFirestore> firestoreProvider, Provider<FirebaseAuth> authProvider) {
    this.geminiApiProvider = geminiApiProvider;
    this.firestoreProvider = firestoreProvider;
    this.authProvider = authProvider;
  }

  @Override
  public TimetableRepository get() {
    return newInstance(geminiApiProvider.get(), firestoreProvider.get(), authProvider.get());
  }

  public static TimetableRepository_Factory create(Provider<GeminiApi> geminiApiProvider,
      Provider<FirebaseFirestore> firestoreProvider, Provider<FirebaseAuth> authProvider) {
    return new TimetableRepository_Factory(geminiApiProvider, firestoreProvider, authProvider);
  }

  public static TimetableRepository newInstance(GeminiApi geminiApi, FirebaseFirestore firestore,
      FirebaseAuth auth) {
    return new TimetableRepository(geminiApi, firestore, auth);
  }
}
