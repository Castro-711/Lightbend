resolvers in ThisBuild += "lightbend-commercial-mvn" at
        "https://repo.lightbend.com/pass/xZC5hKXo3izl3lKTEzqwWYHMVwjZHv3B6ouQLsHQnZZSDX-G/commercial-releases"
resolvers in ThisBuild += Resolver.url("lightbend-commercial-ivy",
        url("https://repo.lightbend.com/pass/xZC5hKXo3izl3lKTEzqwWYHMVwjZHv3B6ouQLsHQnZZSDX-G/commercial-releases"))(Resolver.ivyStylePatterns)
