package co.gov.cancilleria.miconsulado.web.rest;

import co.gov.cancilleria.miconsulado.MiconsuladogatewayApp;
import co.gov.cancilleria.miconsulado.domain.main.Kanbam;
import co.gov.cancilleria.miconsulado.domain.main.enumeration.State;
import co.gov.cancilleria.miconsulado.repository.main.KanbamRepository;
import co.gov.cancilleria.miconsulado.service.KanbamService;
import co.gov.cancilleria.miconsulado.service.dto.KanbamDTO;
import co.gov.cancilleria.miconsulado.service.mapper.KanbamMapper;
import co.gov.cancilleria.miconsulado.web.rest.errors.ExceptionTranslator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static co.gov.cancilleria.miconsulado.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
/**
 * Integration tests for the {@Link KanbamResource} REST controller.
 */
@SpringBootTest(classes = MiconsuladogatewayApp.class)
public class KanbamResourceIT {

    private static final Long DEFAULT_SIZE = 1L;
    private static final Long UPDATED_SIZE = 2L;

    private static final State DEFAULT_STATE = State.TO_DO;
    private static final State UPDATED_STATE = State.DOING;

    @Autowired
    private KanbamRepository kanbamRepository;

    @Autowired
    private KanbamMapper kanbamMapper;

    @Autowired
    private KanbamService kanbamService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    @Qualifier("defaultEntityManagerFactory")
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restKanbamMockMvc;

    private Kanbam kanbam;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final KanbamResource kanbamResource = new KanbamResource(kanbamService);
        this.restKanbamMockMvc = MockMvcBuilders.standaloneSetup(kanbamResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Kanbam createEntity(EntityManager em) {
        Kanbam kanbam = new Kanbam()
            .size(DEFAULT_SIZE)
            .state(DEFAULT_STATE);
        return kanbam;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Kanbam createUpdatedEntity(EntityManager em) {
        Kanbam kanbam = new Kanbam()
            .size(UPDATED_SIZE)
            .state(UPDATED_STATE);
        return kanbam;
    }

    @BeforeEach
    public void initTest() {
        kanbam = createEntity(em);
    }

    @Test
    @Transactional
    public void createKanbam() throws Exception {
        int databaseSizeBeforeCreate = kanbamRepository.findAll().size();

        // Create the Kanbam
        KanbamDTO kanbamDTO = kanbamMapper.toDto(kanbam);
        restKanbamMockMvc.perform(post("/api/kanbams")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(kanbamDTO)))
            .andExpect(status().isCreated());

        // Validate the Kanbam in the database
        List<Kanbam> kanbamList = kanbamRepository.findAll();
        assertThat(kanbamList).hasSize(databaseSizeBeforeCreate + 1);
        Kanbam testKanbam = kanbamList.get(kanbamList.size() - 1);
        assertThat(testKanbam.getSize()).isEqualTo(DEFAULT_SIZE);
        assertThat(testKanbam.getState()).isEqualTo(DEFAULT_STATE);
    }

    @Test
    @Transactional
    public void createKanbamWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = kanbamRepository.findAll().size();

        // Create the Kanbam with an existing ID
        kanbam.setId(1L);
        KanbamDTO kanbamDTO = kanbamMapper.toDto(kanbam);

        // An entity with an existing ID cannot be created, so this API call must fail
        restKanbamMockMvc.perform(post("/api/kanbams")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(kanbamDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Kanbam in the database
        List<Kanbam> kanbamList = kanbamRepository.findAll();
        assertThat(kanbamList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllKanbams() throws Exception {
        // Initialize the database
        kanbamRepository.saveAndFlush(kanbam);

        // Get all the kanbamList
        restKanbamMockMvc.perform(get("/api/kanbams?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(kanbam.getId().intValue())))
            .andExpect(jsonPath("$.[*].size").value(hasItem(DEFAULT_SIZE.intValue())))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())));
    }
    
    @Test
    @Transactional
    public void getKanbam() throws Exception {
        // Initialize the database
        kanbamRepository.saveAndFlush(kanbam);

        // Get the kanbam
        restKanbamMockMvc.perform(get("/api/kanbams/{id}", kanbam.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(kanbam.getId().intValue()))
            .andExpect(jsonPath("$.size").value(DEFAULT_SIZE.intValue()))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingKanbam() throws Exception {
        // Get the kanbam
        restKanbamMockMvc.perform(get("/api/kanbams/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateKanbam() throws Exception {
        // Initialize the database
        kanbamRepository.saveAndFlush(kanbam);

        int databaseSizeBeforeUpdate = kanbamRepository.findAll().size();

        // Update the kanbam
        Kanbam updatedKanbam = kanbamRepository.findById(kanbam.getId()).get();
        // Disconnect from session so that the updates on updatedKanbam are not directly saved in db
        em.detach(updatedKanbam);
        updatedKanbam
            .size(UPDATED_SIZE)
            .state(UPDATED_STATE);
        KanbamDTO kanbamDTO = kanbamMapper.toDto(updatedKanbam);

        restKanbamMockMvc.perform(put("/api/kanbams")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(kanbamDTO)))
            .andExpect(status().isOk());

        // Validate the Kanbam in the database
        List<Kanbam> kanbamList = kanbamRepository.findAll();
        assertThat(kanbamList).hasSize(databaseSizeBeforeUpdate);
        Kanbam testKanbam = kanbamList.get(kanbamList.size() - 1);
        assertThat(testKanbam.getSize()).isEqualTo(UPDATED_SIZE);
        assertThat(testKanbam.getState()).isEqualTo(UPDATED_STATE);
    }

    @Test
    @Transactional
    public void updateNonExistingKanbam() throws Exception {
        int databaseSizeBeforeUpdate = kanbamRepository.findAll().size();

        // Create the Kanbam
        KanbamDTO kanbamDTO = kanbamMapper.toDto(kanbam);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restKanbamMockMvc.perform(put("/api/kanbams")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(kanbamDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Kanbam in the database
        List<Kanbam> kanbamList = kanbamRepository.findAll();
        assertThat(kanbamList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteKanbam() throws Exception {
        // Initialize the database
        kanbamRepository.saveAndFlush(kanbam);

        int databaseSizeBeforeDelete = kanbamRepository.findAll().size();

        // Delete the kanbam
        restKanbamMockMvc.perform(delete("/api/kanbams/{id}", kanbam.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database is empty
        List<Kanbam> kanbamList = kanbamRepository.findAll();
        assertThat(kanbamList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Kanbam.class);
        Kanbam kanbam1 = new Kanbam();
        kanbam1.setId(1L);
        Kanbam kanbam2 = new Kanbam();
        kanbam2.setId(kanbam1.getId());
        assertThat(kanbam1).isEqualTo(kanbam2);
        kanbam2.setId(2L);
        assertThat(kanbam1).isNotEqualTo(kanbam2);
        kanbam1.setId(null);
        assertThat(kanbam1).isNotEqualTo(kanbam2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(KanbamDTO.class);
        KanbamDTO kanbamDTO1 = new KanbamDTO();
        kanbamDTO1.setId(1L);
        KanbamDTO kanbamDTO2 = new KanbamDTO();
        assertThat(kanbamDTO1).isNotEqualTo(kanbamDTO2);
        kanbamDTO2.setId(kanbamDTO1.getId());
        assertThat(kanbamDTO1).isEqualTo(kanbamDTO2);
        kanbamDTO2.setId(2L);
        assertThat(kanbamDTO1).isNotEqualTo(kanbamDTO2);
        kanbamDTO1.setId(null);
        assertThat(kanbamDTO1).isNotEqualTo(kanbamDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(kanbamMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(kanbamMapper.fromId(null)).isNull();
    }
}
