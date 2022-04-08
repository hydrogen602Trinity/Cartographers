
def fields_to_constructor(s):
    ls = [e.strip() for e in s.strip().split('\n')]

    ls = [e.split() for e in ls if not e.startswith('@') and e]

    get_set = []

    t1 = []
    t2 = []

    for row in ls:
        name: str
        type_: str
        _, type_, name = row

        if name.endswith(';'):
            name = name[:-1]

        c_name = name.capitalize()

        t1.append(f'{type_} {name}')
        t2.append(f'this.{name} = {name}')

        get_set.append(
            f'public {type_} get{c_name}() {{ return this.{name}; }}\n\npublic void set{c_name}({type_} {name}) {{ this.{name} = {name}; }}')

    print('\n\n'.join(get_set))

    s = ";\n".join(t2)

    print(f'public CLASS_NAME({", ".join(t1)}) {{\n{s}\n}}')


fields_to_constructor('''    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "upload_date")
    private long upload_date;

    @Column(name = "author")
    private String author;

    @Column(name = "length")
    private String length;

    @Column(name = "objective_main")
    private int objective_main;

    @Column(name = "objective_bonus")
    private int objective_bonus;

    @Column(name = "difficulty")
    private String difficulty;

    @Column(name = "description_short")
    private String description_short;

    @Column(name = "download_count")
    private long download_count;

    @Column(name = "type")
    private String type;

    @Column(name = "image_url")
    private String image_url;

    @Column(name = "series")
    private String series;

    @Column(name = "mc_version")
    private String mc_version;
''')
