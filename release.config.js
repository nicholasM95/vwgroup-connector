const publish = ['@semantic-release/github'];

const analyzeCommits = [
    {
        path: '@semantic-release/commit-analyzer',
        releaseRules: [
            { type: "feat", release: "minor" },
            { type: "fix", release: "patch" },
            { type: "breaking", release: "major" },
            { type: "refactor", release: "patch" },
            { type: "chore", release: "patch" },
            { type: "docs", release: "patch" },
        ]
    }
];

const generateNotes = [
    {
        path: '@semantic-release/release-notes-generator',
        writerOpts: {
            groupBy: "type",
            commitGroupsSort: ["breaking", "feat", "fix", "chore"],
            commitsSort: "header"
        },
        types: [
            { type: "feat", section: "Features" },
            { type: "fix", section: "Bug Fixes" },
            { type: "breaking", section: "Breaking Changes" },
            { type: "chore", section: "Chore" },
            { type: "refactor", hidden: true },
            { type: "docs", hidden: true },
            { type: "doc", hidden: true },
            { type: "style", hidden: true },
            { type: "perf", hidden: true },
            { type: "test", hidden: true }
        ],
        presetConfig: true
    }
];

const prepare = [
    {
        path: '@semantic-release/changelog',
        changelogFile: 'CHANGELOG.md'
    }
]

const config = {
    branches: [
        'main',
        {name: 'develop', prerelease: 'SNAPSHOT'}
    ],
    plugins: [
        '@semantic-release/commit-analyzer',
        '@semantic-release/release-notes-generator',
        '@semantic-release/changelog',
        '@semantic-release/github',
        ['@semantic-release/exec', {
            'verifyReleaseCmd': './update-version.sh $BRANCH_NAME ${nextRelease.version}',
        }]
    ],
    analyzeCommits: analyzeCommits,
    generateNotes: generateNotes,
    prepare: prepare,
    publish: publish

};

module.exports = config;
